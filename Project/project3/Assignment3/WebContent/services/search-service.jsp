<%@ page contentType="text/html; charset=utf-8" language="java"
	import="ir.assignments.four.web.*"
	import="java.net.URLEncoder"
	import="ir.assignments.four.helpers.DateHelper"%>
<%
	response.setHeader("Cache-Control", "private");
	response.setHeader("Expires", DateHelper.getExpirationDate(30)); // 30 second cache

	// This is the service that performs the search on the server and then returns the results

	// Input Parameters
	String query = request.getParameter("query");
	String pageStr = request.getParameter("page");
	int maxPerPage = 10;

	int currentPage = 0;
	try {
		if (pageStr != null)
			currentPage = Integer.parseInt(pageStr) - 1;

		if (currentPage < 0)
			currentPage = 0;
		else if (currentPage > 20) // limit to top 20 pages
			currentPage = 20;
	}
	catch (Exception e) {
	}

	// Do search
	WebSearch searcher = null;
	WebResultSet results = null;
	try {
		searcher = new WebSearch();
		results = searcher.search(query, currentPage, maxPerPage);
	}
	finally {
		if (searcher != null)
			searcher.close();
	}

	// Render the results
	if (results != null && results.getResults().length > 0) {
		String suggestedQuery = results.getSuggestedQuery();
		if (suggestedQuery != null && !suggestedQuery.equals("")) {
			String urlSuggestedQuery = URLEncoder.encode(suggestedQuery.replace("<b><i>", "").replace("</i></b>", ""), "UTF-8");
			out.println("<div class=\"suggestion\">Did you mean: <a href=\"search.jsp?query=" + urlSuggestedQuery + "\">" + suggestedQuery + "</a></div>");
		}

		for (SearchResult result : results.getResults()) {
			out.println("<div class=\"search-result\">");

			// Title
			out.println("<a href=\"" + result.getUrl() + "\" class=\"title\">");
			out.println(result.getTitle());
			out.println("</a>");

			// Url
			out.println("<div class=\"url\">");
			out.println(result.getDisplayUrl());
			out.println("</div>");

			// Description
			out.println("<div class=\"description\">");
			out.println(result.getDescription());
			out.println("</div>");

			out.println("</div>");
		}

		// Page selector
		out.println("<div class=\"pagination\">");
		out.println("<ul>");
		int totalPages = results.getTotalPages(maxPerPage);
		for (int i = 0; i < totalPages; i++) {
			if (i == currentPage) {
				out.println("<li class=\"active\"><a href=\"search.jsp?query=" + URLEncoder.encode(query, "UTF-8") + "&page=" + (i + 1) + "" + "\">" + (i + 1) + "</a></li>");
			}
			else {
				out.println("<li><a href=\"search.jsp?query=" + URLEncoder.encode(query, "UTF-8") + "&page=" + (i + 1) + "" + "\">" + (i + 1) + "</a></li>");
			}
		}
		out.println("</ul>");
		out.println("</div>");
	}
	else {
		out.println("<div class=\"no-results\">No search results found</div>");
	}
%>