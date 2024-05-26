<%@ page contentType="text/html;charset=UTF-8" language="java" %> 
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/film-cards.css">
<link rel="icon" href="${pageContext.request.contextPath}/images/favicon.ico" type="image/x-icon">
<!-- Google Fonts -->
<link rel="preconnect" href="https://fonts.googleapis.com">
<link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
<link
	href="https://fonts.googleapis.com/css2?family=Alegreya:ital@0;1&family=Cabin:ital,wght@0,400..700;1,400..700&family=Fira+Sans:ital,wght@0,100;0,200;0,300;0,400;0,500;0,600;0,700;0,800;0,900;1,100;1,200;1,300;1,400;1,500;1,600;1,700;1,800;1,900&family=Lexend&display=swap"
	rel="stylesheet">
<!-- Google Fonts -->

<div id="film-container" class="film-container" data-context-path="${pageContext.request.contextPath}">
    
</div>

<button class="load-more-button" onclick="loadMoreFilms()">Load More</button>

<div class="loading-overlay" id="loadingOverlay">
    <div class="spinner"></div>
</div>

<script src="${pageContext.request.contextPath}/js/film-cards.js"></script>

