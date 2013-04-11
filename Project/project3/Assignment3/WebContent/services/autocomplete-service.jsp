<%@ page contentType="text/html; charset=utf-8" language="java"
	import="ir.assignments.four.web.*"
	import="java.net.URLEncoder" %>
<%
	// This is the service that gives the list of terms for the autosuggest

	// Input Parameters
	String query = request.getParameter("query");

	if (query != null) {
		// Search for possible completions of the query
		WebSearch searcher = null;
		String[] completions = null;
		try {
			searcher = new WebSearch();
			completions = searcher.getAutoCompleteSuggestions(query);
		}
		finally {
			if (searcher != null)
				searcher.close();
		}

		// Render the results
		if (completions != null && completions.length > 0) {
			for (String completion : completions) {
				out.println("<div class=\"item\">" + query + "<strong>" + completion + "</strong></div>");
			}
		}
	}
%>