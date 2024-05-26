<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<script src="${pageContext.request.contextPath}/js/search-box.js"></script>
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/search-box.css"/>
<link rel="icon" href="${pageContext.request.contextPath}/images/favicon.ico" type="image/x-icon">
<!-- Google Fonts -->
<link rel="preconnect" href="https://fonts.googleapis.com">
<link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
<link
	href="https://fonts.googleapis.com/css2?family=Alegreya:ital@0;1&family=Cabin:ital,wght@0,400..700;1,400..700&family=Fira+Sans:ital,wght@0,100;0,200;0,300;0,400;0,500;0,600;0,700;0,800;0,900;1,100;1,200;1,300;1,400;1,500;1,600;1,700;1,800;1,900&family=Lexend&display=swap"
	rel="stylesheet">
<!-- Google Fonts -->
<div class="search-box">
    <input type="text" id="film-search-input" placeholder="Search by ID, Title, Year, Director or Star..." onkeyup="searchFilms()"/>
</div>
