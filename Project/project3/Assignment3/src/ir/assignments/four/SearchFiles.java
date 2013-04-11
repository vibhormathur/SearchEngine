package ir.assignments.four;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.en.EnglishAnalyzer;
import org.apache.lucene.analysis.miscellaneous.PerFieldAnalyzerWrapper;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.BooleanClause.Occur;
import org.apache.lucene.search.Sort;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.PhraseQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.SortField;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.highlight.Highlighter;
import org.apache.lucene.search.highlight.InvalidTokenOffsetsException;
import org.apache.lucene.search.highlight.NullFragmenter;
import org.apache.lucene.search.highlight.QueryScorer;
import org.apache.lucene.search.highlight.SimpleHTMLFormatter;
import org.apache.lucene.search.spans.SpanFirstQuery;
import org.apache.lucene.search.spans.SpanNearQuery;
import org.apache.lucene.search.spans.SpanQuery;
import org.apache.lucene.search.spans.SpanTermQuery;
import org.apache.lucene.search.spell.SpellChecker;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

public class SearchFiles {
	private IndexReader reader;
	private IndexReader autoCompleteReader;
	private IndexSearcher searcher;
	private IndexSearcher autoCompleteSearcher;
	private SpellChecker spellChecker;
	private Analyzer analyzer;
	
	public SearchFiles(String indexLocation) throws IOException {
		this.reader = DirectoryReader.open(FSDirectory.open(new File(indexLocation)));		
		this.searcher = new IndexSearcher(reader);
		
		Analyzer regularAnalyzer = new StandardAnalyzer(Version.LUCENE_41);	
		Analyzer stemAnalyzer = new EnglishAnalyzer(Version.LUCENE_41);
		Analyzer autoCompleteAnalyzer = new AutoCompleteAnalyzer(); //new ShingleAnalyzerWrapper(new StandardAnalyzer(Version.LUCENE_41), 3);
		
		// Use the stem analyzer for some fields, the regular analyzer for the rest
		// Note: Should match mapping used in Indexer.java
		Map<String,Analyzer> analyzerPerField = new HashMap<String,Analyzer>();
		analyzerPerField.put("stemcontent", stemAnalyzer);
		analyzerPerField.put("stemtitle", stemAnalyzer);
		analyzerPerField.put("stemanchortext", stemAnalyzer);
		analyzerPerField.put("ngram", autoCompleteAnalyzer);

		this.analyzer = new PerFieldAnalyzerWrapper(regularAnalyzer, analyzerPerField);
	}
 	
	public SearchFiles(String indexLocation, String autoCompleteIndexLocation) throws IOException {
		this(indexLocation);

		this.autoCompleteReader = DirectoryReader.open(FSDirectory.open(new File(autoCompleteIndexLocation)));
		this.autoCompleteSearcher = new IndexSearcher(autoCompleteReader);
	}
	
	public SearchFiles(String indexLocation, String autoCompleteIndexLocation, String spellCheckerIndexLocation) throws IOException {
		this(indexLocation, autoCompleteIndexLocation);
		
		Directory spellCheckIndexDir = FSDirectory.open(new File(spellCheckerIndexLocation));
		this.spellChecker = new SpellChecker(spellCheckIndexDir);
	}
	
	public ResultSet search(String queryString, int page, int maxPerPage) {
		
		// Do search by combining the query among different fields with different boosting weights
		try {
			// Build query
			Query[] queries = new Query[]
			{
				getFieldQuery(queryString, "urldomain", 123.3f),
				
				getFieldPhraseQuery(queryString, "title", 3.4f),
				getFieldQuery(queryString, "stemtitle", 51.2f),
				
				getFieldQuery(queryString, "content", 211.9f),
				getFieldQuery(queryString, "stemcontent", 19.2f),

				getFieldQuery(queryString, "importantcontent", 296.0f),
				
				getFieldQuery(queryString, "outgoingtext", 3.9f),
				getFieldQuery(queryString, "anchortext", 2.3f),
				getFieldQuery(queryString, "stemanchortext", 214.5f),
				
				getFieldNearQuery(queryString, "title", 4.8f),
				getFieldNearQuery(queryString, "stemtitle", 112.2f),
				getFieldNearQuery(queryString, "content", 91.0f),
				getFieldNearQuery(queryString, "importantcontent", 1.1f)
		    };
			
			BooleanQuery finalQuery = new BooleanQuery();
			for (Query query : queries) {
				if (query.getBoost() > 0.0f)
					finalQuery.add(query, Occur.SHOULD);
			}

			return doPagingSearch(finalQuery, page, maxPerPage);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	public ResultSet search(String queryString, int page, int maxPerPage, float[] weightVector) {
		// Used by SearchOptimizer.java to find the best weights for the test queries
		
		try {
			// Build query
			Query[] queries = new Query[]
			{
				getFieldQuery(queryString, "url", weightVector[0]),
				getFieldQuery(queryString, "urldomain", weightVector[1]),
				
				getFieldQuery(queryString, "title", weightVector[2]),
				getFieldPhraseQuery(queryString, "title", weightVector[3]),
				getFieldQuery(queryString, "stemtitle", weightVector[4]),
				getFieldPhraseQuery(queryString, "stemtitle", weightVector[5]),
				
				getFieldQuery(queryString, "content", weightVector[6]),
				getFieldQuery(queryString, "stemcontent", weightVector[7]),
				getFieldPhraseQuery(queryString, "content", weightVector[8]),
				
				getFieldQuery(queryString, "contentheaders", weightVector[9]),
				getFieldQuery(queryString, "importantcontent", weightVector[10]),
				
				getFieldQuery(queryString, "outgoingtext", weightVector[11]),
				getFieldQuery(queryString, "anchortext", weightVector[12]),
				getFieldQuery(queryString, "stemanchortext", weightVector[13]),
				
				getFieldNearQuery(queryString, "title", weightVector[14]),
				getFieldNearQuery(queryString, "stemtitle", weightVector[15]),
				getFieldNearQuery(queryString, "content", weightVector[16]),
				getFieldNearQuery(queryString, "stemcontent", weightVector[17]),
				getFieldNearQuery(queryString, "contentheaders", weightVector[18]),
				getFieldNearQuery(queryString, "importantcontent", weightVector[19])
			};

			BooleanQuery finalQuery = new BooleanQuery();
			for (Query query : queries) {
				if (query.getBoost() > 0.0f) {
					finalQuery.add(query, Occur.SHOULD);
				}
			}
			
			return doPagingSearch(finalQuery, page, maxPerPage);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	private Query getFieldQuery(String queryString, String field, float boost) throws ParseException {
		// Build a default query for the field
		QueryParser parser = new QueryParser(Version.LUCENE_41, field, this.analyzer);
		Query query = parser.parse(queryString);
		query.setBoost(boost);
		return query;		
	}
	
	private Query getFieldSpanQuery(String queryString, String field, float boost) throws ParseException, IOException {
		// Build a query that matches documents were the query string is in the beginning of the field
		// e.g. this can be used to give preference to title's with the query strings near the beginning
		
		// Split the query string into tokens
		TokenStream tokens = this.analyzer.tokenStream(field, new StringReader(queryString));
		tokens.reset();
		
		BooleanQuery query = new BooleanQuery();
		
		// Allow each token to be k terms from the start (starting with 1)
		int k = 1;
		while (tokens.incrementToken()) {
			String word = tokens.getAttribute(CharTermAttribute.class).toString();
			SpanTermQuery spanTermQuery = new SpanTermQuery(new Term(field, word));
			SpanFirstQuery spanFirstQuery = new SpanFirstQuery(spanTermQuery, k);
			k++;
			
			query.add(spanFirstQuery, Occur.SHOULD);
		}
		query.setBoost(boost);
		
		return query;		
	}
	
	private Query getFieldPhraseQuery(String queryString, String field, float boost) throws ParseException, IOException {
		// Build a query that matches the query string as an exact phrase
		TokenStream tokens = this.analyzer.tokenStream(field, new StringReader(queryString));
		tokens.reset();
		
		PhraseQuery query = new PhraseQuery();
		while (tokens.incrementToken()) {
			String word = tokens.getAttribute(CharTermAttribute.class).toString();
			query.add(new Term(field, word));
		}
		query.setBoost(boost);
		
		return query;
	}
	
	private Query getFieldNearQuery(String queryString, String field, float boost) throws ParseException, IOException {
		// Build a query that matches the query terms close to each other
		ArrayList<SpanQuery> clausesList = new ArrayList<SpanQuery>();
        TokenStream tokens = this.analyzer.tokenStream(field, new StringReader(queryString));
        tokens.reset();
        
        while (tokens.incrementToken()) {
        	String word = tokens.getAttribute(CharTermAttribute.class).toString();
        	Term term = new Term(field, word);
        	clausesList.add(new SpanTermQuery(term));
        }
        
        SpanQuery[] clauses = clausesList.toArray(new SpanQuery[clausesList.size()]);

        SpanNearQuery spannearQuery = new SpanNearQuery(clauses, 5, true);
        spannearQuery.setBoost(boost);
        return spannearQuery;    
    }
	
	private ResultSet doPagingSearch(Query query, int page, int maxPerPage) throws IOException {
		
		// Do the search
		TopDocs results = searcher.search(query, (page * maxPerPage) + maxPerPage);
		ScoreDoc[] hits = results.scoreDocs;
		
		int start = page * maxPerPage; // first page is page 0
		int end = Math.min((page + 1) * maxPerPage, results.totalHits);

		ArrayList<String> urls = new ArrayList<String>();
		
		for (int i = start; i < end; i++) {
			// Results are in docIds, find the indexed document
			int docId = hits[i].doc;
			Document doc = searcher.doc(docId);
			
			// Save the URL of the document
			String url = doc.get("url");			
			urls.add(url);
		}
		
		return new ResultSet(urls, results.totalHits);
	}
	
	// Code borrowed from: http://stackoverflow.com/questions/120180/how-to-do-query-auto-completion-suggestions-in-lucene
	public Set<String> suggestTerms(String term) {
		HashSet<String> terms = new HashSet<String>();
		
		if (term == null || this.autoCompleteReader == null)
			return terms;
		
		try {
			// Search the "ngram" field (using the ngram analyzer) and return the
			// few top results
			TermQuery query = new TermQuery(new Term("ngram", term.toLowerCase()));
			Sort docFreqSort = new Sort(new SortField("docFreq", SortField.Type.INT)); // order by doc frequency (more common terms first)
			
			TopDocs results = autoCompleteSearcher.search(query, 10, docFreqSort);
			ScoreDoc[] hits = results.scoreDocs;
			
			for (int i = 0; i < Math.min(10, hits.length); i++) {
				// Results are in docIds, find the indexed document
				Document doc = autoCompleteSearcher.doc(hits[i].doc);
				
				// Save the original term
				String originalTerm = doc.get("original");
				terms.add(originalTerm);
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		return terms;
	}
	
	public String getHighlights(String fieldName, String queryString, String contentText, boolean entireDocument) throws IOException, InvalidTokenOffsetsException, ParseException{
		StringBuilder output = new StringBuilder();
		
		// The highlighter takes in the HTML formatter (since we want to bold the terms)
		// and the query string parsed (using the default query parser with default boost 1.0)
		SimpleHTMLFormatter htmlFormatter = new SimpleHTMLFormatter();
		Query query = getFieldQuery(queryString, fieldName, 1.0f);		
		Highlighter highlighter = new Highlighter(htmlFormatter, new QueryScorer(query));
		if (entireDocument) {
			highlighter.setTextFragmenter(new NullFragmenter()); // don't break up the text
		}
		
		// Concatenate the top fragment matches
		int maxFragments = 2;
		String[] fragments = highlighter.getBestFragments(this.analyzer, fieldName, contentText, maxFragments);		
		for (String fragment : fragments) {
			output.append(fragment.trim());
			if (!entireDocument)
				output.append("... ");
			else
				output.append(" ");
		}
		
		String outputStr = output.toString().trim();
		if (outputStr.equals(""))
			outputStr = contentText; // return original text if nothing was found to highlight
		
		return outputStr;
	}
	
	public boolean hasMisspelledWords(String query) {
		try {
			String[] tokens = query.trim().split(" ");
			for (String token : tokens) {
				if (!Character.isUpperCase(token.charAt(0)) && !this.spellChecker.exist(token)) { // ignore token where first letter is capitalized (probably a name)
					return true;
				}
			}
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		
		return false;
	}
	
	// Code borrow from: http://www.javacodegeeks.com/2010/05/did-you-mean-feature-lucene-spell.html
	public String spellChecker(String query, int minimumResults) {
		// Find suggestions for each misspelled token to generate some alternative queries
		String suggestedQuery = "";
		
		try {
			// Generate alternative queries where the misspelled words are replaced
			// (very simple algorithm, no fancy mix-and-matching of corrected words)
			HashSet<String> suggestedQueries = new HashSet<String>();			
			String[] tokens = query.trim().split(" ");
			for (String token : tokens) {
				if (token.length() == 0)
					continue;
				
				if (!Character.isUpperCase(token.charAt(0)) && !this.spellChecker.exist(token)) { // ignore token where first letter is capitalized (probably a name)
					String[] suggestions = this.spellChecker.suggestSimilar(token, 2);
					for (String suggestion : suggestions) {
						String newQuery = query.replace(token, "<b><i>" + suggestion + "</i></b>");
						suggestedQueries.add(newQuery);
						
						if (suggestedQueries.size() >= 5) { // limit total number of alternative queries to keep function relatively fast
							break;
						}
					}
				}
				
				if (suggestedQueries.size() >= 5) {
					break;
				}
			}
			
			// Find the suggestion with the most results
			int maxResultCount = 0;
			for (String possibleSuggestedQuery : suggestedQueries) {
				int resultCount = search(possibleSuggestedQuery.replace("<b><i>", "").replace("</i></b>", ""), 1, 10).getTotalHits();
				if (resultCount >= minimumResults && // has enough results
				    (resultCount > maxResultCount || // has most results so far... 
				     (resultCount == maxResultCount && possibleSuggestedQuery.length() < suggestedQuery.length()))) { // ...or has the same amount of results but its shorter
					maxResultCount = resultCount;
					suggestedQuery = possibleSuggestedQuery;
				}
			}
			
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		
		return suggestedQuery;
	}
	
	public void close(){
		if (reader != null) {
			try {
				reader.close();
				analyzer.close();
				if (autoCompleteReader != null)
					autoCompleteReader.close();
			}
			catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
