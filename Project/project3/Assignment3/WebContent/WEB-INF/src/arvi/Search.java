package arvi;

public class Search
{	
	private Cache<MovieEntry> cache;
	private Connection db;
	private String searchQuery;
	private int maxPerPage;
	private boolean ranSearch;
	
	public Search(String searchQuery, int maxPerPage)
	{
		this.cache = new Cache<MovieEntry>();
		this.db = Database.getConnection();
		this.searchQuery = searchQuery;
		this.maxPerPage = maxPerPage;
	}
	
	public void updateMaxPerPage(int max)
	{
		if (max > 0)
			this.maxPerPage = max;
	}
	
	public int getMaxPerPage()
	{
		return this.maxPerPage;
	}
	
	private String extractValue(String allParams, String parameter)
	{
		String value = "";
		int index = allParams.indexOf(parameter + ":");
		if (index != -1)
		{
			int start = index + parameter.length() + 1;
			int end = allParams.indexOf(';', start);
			if (end == -1)
				end = allParams.length();
			
			if (end > start)
				value = allParams.substring(start, end);
		}
		
		return value;
	}
	
	private void saveEntry(ResultSet row)
	{
		try
		{
			int id = row.getInt("movie_id");
			if (id > 0)
			{
				MovieEntry entry = new MovieEntry(id);
				
				//Check if movie added before
				MovieEntry previousEntry = cache.search(entry);
				
				if (previousEntry != null) //already existed
				{
					//Use the original object
					entry = previousEntry;
				}
				else
				{
					//Create new movie
					entry.Title = row.getString("title");
					entry.Year = row.getInt("year");
					entry.Director = row.getString("director");
					entry.BannerURL = row.getString("banner_url");
					if (entry.BannerURL == null) entry.BannerURL = "";
					entry.TrailerURL = row.getString("trailer_url");
					if (entry.TrailerURL == null) entry.TrailerURL = "";
					cache.add(entry); //add it to the cache
				}			
				
				//Add genre
				entry.Genres.add(row.getString("name"));
				
				//Add movie star
				int starID = row.getInt("star_id");
				if (starID > 0)
				{
					MovieStar star = new MovieStar(starID);
					if (!entry.Stars.contains(star))
					{
						//Construct new movie star
						star.FirstName = row.getString("first_name");
						star.LastName = row.getString("last_name");
						star.PhotoURL = row.getString("photo_url");
						if (star.PhotoURL == null) star.PhotoURL = "";
						star.DOB = row.getDate("dob");
						if (star.DOB == null) star.DOB = new java.util.Date();
						entry.Stars.add(star);
					}
				}				
			}			
		}
		catch (SQLException e)
		{
		}
	}
	
	private static String getJoinedTables()
	{
		return "movies M LEFT JOIN stars_in_movies SM " +
		        "ON M.id=SM.movie_id LEFT JOIN stars S " +
		        "ON SM.star_id=S.id LEFT JOIN genres_in_movies GM " +
		        "ON GM.movie_id=M.id LEFT JOIN genres G " +
		        "ON GM.genre_id=G.id";
	}
	
	private String buildQuery(String orderBy, Order orderDirection, ArrayList<String> criteria)
	{
		//Get possible search criteria
		String title = "%" + extractValue(searchQuery, "title") + "%";
		String year = "%" + extractValue(searchQuery, "year") + "%";
		String director = "%" + extractValue(searchQuery, "director") + "%";
		String starFirstName = "%" + extractValue(searchQuery, "firstname") + "%";
		String starLastName = "%" + extractValue(searchQuery, "lastname") + "%";
		String browseTitle = extractValue(searchQuery, "browseT") + "%";
		String browseGenre = extractValue(searchQuery, "browseG");
		
		//Build SQL query
		String tables = getJoinedTables();
		String query = "SELECT * FROM " +
					   tables +
		               " WHERE";
		
		if (title.length() > 2)
		{
			query += " title LIKE ?";
			criteria.add(title);
		}
		
		if (year.length() > 2)
		{
			query += criteria.size() > 0 ? " AND" : "";
			query += " year LIKE ?";
			criteria.add(year);
		}
		
		if (director.length() > 2)
		{
			query += criteria.size() > 0 ? " AND" : "";
			query += " director LIKE ?";
			criteria.add(director);
		}
		
		if (starFirstName.length() > 2)
		{
			query += criteria.size() > 0 ? " AND" : "";
			query += " first_name LIKE ?";
			criteria.add(starFirstName);
		}
		
		if (starLastName.length() > 2)
		{
			query += criteria.size() > 0 ? " AND" : "";
			query += " last_name LIKE ?";
			criteria.add(starLastName);
		}
		
		if (browseTitle.length() > 1)
		{
			if (browseTitle.equals("0%"))
			{
				//Special case: 0 = numbers
				query += " title REGEXP '^[0-9]'";
			}
			else
			{
				query += " title LIKE ?";			
				criteria.add(browseTitle);
			}
		}
		
		if (browseGenre.length() > 0)
		{
			query += " genre_id=?";
			criteria.add(browseGenre);
		}
		
		if (orderBy.length() > 2)
		{
			query += " ORDER BY " + orderBy;
			if (orderDirection == Order.ASCENDING)
				query += " ASC";
			else if (orderDirection == Order.DESCENDING)
				query += " DESC";
		}
		
		//query += " LIMIT 200";

		return query;
	}
	
	private void doSearch()
	{
		doSearch("", Order.DEFAULT);
	}
	
	private void doSearch(String orderBy, Order orderDirection)
	{		
		ArrayList<String> values = new ArrayList<String>();
		String query = buildQuery(orderBy, orderDirection, values);
		
		//Execute query
		if (values.size() > -1) //searching by something
		{
			ResultSet results = Database.runQuery(db, query, values.toArray());

			//Save up to 200 results
			int maxResults = 200;
			int count = 0;
			try
			{
				while (results != null && results.next()) //&& count < maxResults)
				{
					count++;
					saveEntry(results);					
				}	
			}
			catch (SQLException e)
			{
			}
		}

		ranSearch = true;
	}
	
	public MovieEntry[] search(int page)
	{
		if (!ranSearch)
			doSearch(); //populate the cache
		
		if (cache.size() == 0)
			return new MovieEntry[0]; //no results
			
		//Get the chunk of records corresponding to the given page
		int startIndex = (page - 1) * maxPerPage;
		int endIndex = Math.min(startIndex + maxPerPage, cache.size());
		
		MovieEntry[] subset = new MovieEntry[endIndex - startIndex];
		cache.inOrder().subList(startIndex, endIndex).toArray(subset);
		
		return subset;
	}
	
	public MovieEntry searchCache(int id)
	{
		return cache.search(new MovieEntry(id));
	}
	
	public void orderBy(String order, Order direction)
	{
		//Clear cache
		cache.clear();
		
		//Run search again with new order
		doSearch(direction != Order.DEFAULT ? order : "", direction);
	}
	
	public int getPageNumbers()
	{
		return (int)Math.ceil(cache.size() / (double)maxPerPage);
	}
	
	public void dispose()
	{
		try 
		{
			db.close();
		} 
		catch (SQLException e) 
		{
		}
		
		cache.clear();
		cache = null;
	}
	
	public static MovieEntry findSingleMovie(int id)
	{
		MovieEntry entry = null;
		
		//Query DB for movie id
		String query = "SELECT * FROM " + Search.getJoinedTables() + " WHERE M.id=?";
		Connection db = Database.getConnection();
		ResultSet result = Database.runQuery(db, query, id);
		
		//Interpret result
		try
		{
			while (result.next())
			{
				if (entry == null)
				{
					entry = new MovieEntry(id);
					entry.Title = result.getString("title");
					entry.Year = result.getInt("year");
					entry.Director = result.getString("director");
					entry.BannerURL = result.getString("banner_url");
					entry.TrailerURL = result.getString("trailer_url");
				}
				
				//Add genre
				entry.Genres.add(result.getString("name"));
				
				//Add movie star
				int starID = result.getInt("star_id");
				if (starID > 0)
				{
					MovieStar star = new MovieStar(starID);
					if (!entry.Stars.contains(star))
					{
						//Construct new movie star
						star.FirstName = result.getString("first_name");
						star.LastName = result.getString("last_name");
						star.PhotoURL = result.getString("photo_url");
						star.DOB = result.getDate("dob");
						entry.Stars.add(star);
					}
				}				
			}
			
			//Close/Relese DB connection
			db.close();
		}
		catch (SQLException e)
		{
		}
		
		return entry;
	}
	
	public static MovieStar findSingleStar(int id)
	{
		//Search in terms of stars instead of movies
		MovieStar entry = null;
		
		//Query DB for movie id
		String query = "SELECT * FROM " + Search.getJoinedTables() + " WHERE S.id=?";
		Connection db = Database.getConnection();
		ResultSet result = Database.runQuery(db, query, id);
		
		//Interpret result
		try
		{
			while (result.next())
			{
				if (entry == null)
				{
					entry = new MovieStar(id);
					entry.FirstName = result.getString("first_name");
					entry.LastName = result.getString("last_name");
					entry.PhotoURL = result.getString("photo_url");
					entry.DOB = result.getDate("dob");
					entry.Movies = new HashSet<MovieEntry>();
				}
				
				//Add movie
				int movieID = result.getInt("movie_id");
				if (movieID > 0)
				{
					MovieEntry movie = new MovieEntry(movieID);
					if (!entry.Movies.contains(movie))
					{
						//Construct new movie star
						movie.Title = result.getString("title");
						movie.Year = result.getInt("year");
						movie.Director = result.getString("director");
						movie.BannerURL = result.getString("banner_url");
						movie.TrailerURL = result.getString("trailer_url");
						entry.Movies.add(movie);
					}
				}				
			}
			
			//Close/Relese DB connection
			db.close();
		}
		catch (SQLException e)
		{
		}
		
		return entry;
	}
}
