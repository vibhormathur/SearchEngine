package ir.assignments.four.web;

public class WebResultSet {
	private SearchResult[] results;
	private int totalHits;
	private int currentPage;
	private String suggestedQuery;
	
	public WebResultSet(SearchResult[] results, int totalHits, int currentPage, String suggestedQuery) {
		this.results = results;
		this.totalHits = totalHits;
		this.currentPage = currentPage;
		this.suggestedQuery = suggestedQuery;
	}
	
	public SearchResult[] getResults() {
		return this.results;
	}
	
	public int getTotalPages(int maxPerPage) {
		return Math.min(totalHits / maxPerPage, 20); // limit to top 20 pages
	}
	
	public int getCurrentPage() { 
		return this.currentPage;
	}
	
	public String getSuggestedQuery() {
		return this.suggestedQuery;
	}
}
