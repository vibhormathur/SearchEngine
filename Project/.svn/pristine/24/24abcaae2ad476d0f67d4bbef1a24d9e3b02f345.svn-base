<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<!DOCTYPE HTML>
<html>
<head>
	<title>ArVi Search</title>
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">	
	
	<!-- Style sheets -->
	<link href="styles/bootstrap-button.css" rel="stylesheet" type="text/css" />
	<link href="styles/bootstrap-pagination.css" rel="stylesheet" type="text/css" />
	<link href="styles/style.css" rel="stylesheet" type="text/css" />	
	
	<!-- Javascript -->
	<script type="text/javascript" src="scripts/jquery-1.9.1.min.js"></script>
	<script type="text/javascript" src="scripts/search.js"></script>
	<script type="text/javascript" src="scripts/autocomplete.js"></script>
</head>
<body>
	<form id="frmSearch" method="GET" action=""> <!-- "Reload" this same page with the new query -->
		<div id="search-container" class="top">
			<div class="inputGroup">
				<h1 id="header-small"><a href="index.jsp">ArVi</a></h1>
				<input type="text" name="query" id="txtQuery" autocomplete="off" value="<%= request.getParameter("query") != null ? request.getParameter("query") : "" %>" />
				<input type="submit" id="btnSearch" class="btn btn-info" value="Search" />
			</div>
		</div>
	</form>
	<div id="loading-image-container">
		<img src="images/loading.gif" /> <!-- No search results by default, these are loaded with JavaScript so page displays quickly -->
	</div>	
	<div id="search-results-container" data-currentpage="<%= request.getParameter("page") != null ? request.getParameter("page") : "1" %>">
	</div>
	<div id="autocomplete">
	</div>
</body>
</html>
