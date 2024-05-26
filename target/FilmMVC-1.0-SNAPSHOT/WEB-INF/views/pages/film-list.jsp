<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html>
<head>
    <title>Film List</title>
    <!-- Google Fonts -->
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Alegreya:ital@0;1&family=Cabin:ital,wght@0,400..700;1,400..700&family=Fira+Sans:ital,wght@0,100;0,200;0,300;0,400;0,500;0,600;0,700;0,800;0,900;1,100;1,200;1,300;1,400;1,500;1,600;1,700;1,800;1,900&family=Lexend&display=swap" rel="stylesheet">
    <!-- Google Fonts -->
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/film-list.css">
    <link rel="icon" href="${pageContext.request.contextPath}/images/favicon.ico" type="image/x-icon">
</head>
<body>
<div id="loadingOverlay" class="loading-overlay">
    <div class="spinner"></div>
</div>
<%@ include file="/WEB-INF/views/shared/header.jsp" %>
<div class="filmListContainer">
    <h1 class="mb-4">Film List</h1>
<div class="search-pagination-container">
    <button id="add-btn" type="button" class="btn btn-success" data-toggle="modal" data-target="#film-add-modal">Add New Film</button>
    <form action="film-list" method="get" class="form-inline search-form">
     <select class="form-control" name="searchType">
            <option value="all">All</option>
            <option value="id">ID</option>
            <option value="title">Title</option>
            <option value="year">Year</option>
            <option value="director">Director</option>
            <option value="stars">Stars</option>
        </select>
        <input type="text" class="form-control" name="searchQuery" placeholder="Search for films...">
        <button type="submit" class="btn btn-primary">Search</button>
        <a href="film-list" class="btn btn-warning clear-search">Clear Search Results</a>
    </form>
     <c:if test="${not empty param.searchQuery}">
      <%
    
      String searchTypeDisplay = "";
    switch (request.getParameter("searchType")) {
        case "id":
            searchTypeDisplay = "ID";
            break;
        case "title":
            searchTypeDisplay = "Title";
            break;
        case "year":
            searchTypeDisplay = "Year";
            break;
        case "director":
            searchTypeDisplay = "Director";
            break;
        case "stars":
            searchTypeDisplay = "Stars";
            break;
        default:
            searchTypeDisplay = "All";
            break;
    }
%>

<h2>Search Type: <%= searchTypeDisplay %></h2>
<h2>Search Results for: "${param.searchQuery}"</h2>
<h2>${filmCount} films found</h2>

    <div style="display:none;" class="pagination">
            <c:if test="${currentPage > 1}">
                <a href="film-list?page=1&searchQuery=${param.searchQuery}&searchType=${param.searchType}" class="btn btn-info">First Page</a>
                <a href="film-list?page=${currentPage - 1}&searchQuery=${param.searchQuery}&searchType=${param.searchType}" class="btn btn-primary">←</a>
            </c:if>
            <c:forEach begin="${startPage}" end="${endPage}" var="i">
                <a href="film-list?page=${i}&searchQuery=${param.searchQuery}&searchType=${param.searchType}" class="${i == currentPage ? 'btn btn-primary' : 'btn btn-default'}">${i}</a>
            </c:forEach>
            <c:if test="${currentPage < totalPages}">
                <a href="film-list?page=${currentPage + 1}&searchQuery=${param.searchQuery}&searchType=${param.searchType}" class="btn btn-primary">→</a>
                <a href="film-list?page=${totalPages}&searchQuery=${param.searchQuery}&searchType=${param.searchType}" class="btn btn-info">Last Page</a>
            </c:if>
            </div>
    </c:if>
    
    <c:if test="${empty param.searchQuery}">
        <div class="pagination">
            <c:if test="${currentPage > 1}">
                <a href="film-list?page=1&searchQuery=${param.searchQuery}&searchType=${param.searchType}" class="btn btn-info">First Page</a>
                <a href="film-list?page=${currentPage - 1}&searchQuery=${param.searchQuery}&searchType=${param.searchType}" class="btn btn-primary">←</a>
            </c:if>
            <c:forEach begin="${startPage}" end="${endPage}" var="i">
                <a href="film-list?page=${i}&searchQuery=${param.searchQuery}&searchType=${param.searchType}" class="${i == currentPage ? 'btn btn-primary' : 'btn btn-default'}">${i}</a>
            </c:forEach>
            <c:if test="${currentPage < totalPages}">
                <a href="film-list?page=${currentPage + 1}&searchQuery=${param.searchQuery}&searchType=${param.searchType}" class="btn btn-primary">→</a>
                <a href="film-list?page=${totalPages}&searchQuery=${param.searchQuery}&searchType=${param.searchType}" class="btn btn-info">Last Page</a>
            </c:if>
        </div>
    </c:if>
</div>
    <table class="table table-striped">
        <thead>
            <tr>
                <th>Poster</th>
                <th>ID</th>
                <th>Title</th>
                <th>Year</th>
                <th>Director</th>
                <th>Stars</th>
                <th>Review</th>
                <th>Action</th>
            </tr>
        </thead>
        <tbody>
            <c:forEach var="film" items="${films}">
                <tr>
                    <td class="filmPoster"><img src="${film.posterUrl}" alt="Poster"></td>
                    <td>${film.id}</td>
                    <td>${film.title}</td>
                    <td>${film.year}</td>
                    <td>${film.director}</td>
                    <td>${film.stars}</td>
                    <td class="review-text">${film.review}</td>
                    <td>
<button class="btn btn-warning editButton" data-toggle="modal" data-target="#film-edit-modal-${film.id}">Edit</button>
<button class="btn btn-danger deleteButton" data-toggle="modal" data-target="#film-delete-modal-${film.id}">Delete</button>
                    </td>
                </tr>
            </c:forEach>
        </tbody>
    </table>
 <c:if test="${not empty param.searchQuery}">
    <div style="display:none;" class="pagination">
            <c:if test="${currentPage > 1}">
                <a href="film-list?page=1&searchQuery=${param.searchQuery}&searchType=${param.searchType}" class="btn btn-info">First Page</a>
                <a href="film-list?page=${currentPage - 1}&searchQuery=${param.searchQuery}&searchType=${param.searchType}" class="btn btn-primary">←</a>
            </c:if>
            <c:forEach begin="${startPage}" end="${endPage}" var="i">
                <a href="film-list?page=${i}&searchQuery=${param.searchQuery}&searchType=${param.searchType}" class="${i == currentPage ? 'btn btn-primary' : 'btn btn-default'}">${i}</a>
            </c:forEach>
            <c:if test="${currentPage < totalPages}">
                <a href="film-list?page=${currentPage + 1}&searchQuery=${param.searchQuery}&searchType=${param.searchType}" class="btn btn-primary">→</a>
                <a href="film-list?page=${totalPages}&searchQuery=${param.searchQuery}&searchType=${param.searchType}" class="btn btn-info">Last Page</a>
            </c:if>
            </div>
    </c:if>
    
    <c:if test="${empty param.searchQuery}">
        <div class="pagination">
            <c:if test="${currentPage > 1}">
                <a href="film-list?page=1&searchQuery=${param.searchQuery}&searchType=${param.searchType}" class="btn btn-info">First Page</a>
                <a href="film-list?page=${currentPage - 1}&searchQuery=${param.searchQuery}&searchType=${param.searchType}" class="btn btn-primary">←</a>
            </c:if>
            <c:forEach begin="${startPage}" end="${endPage}" var="i">
                <a href="film-list?page=${i}&searchQuery=${param.searchQuery}&searchType=${param.searchType}" class="${i == currentPage ? 'btn btn-primary' : 'btn btn-default'}">${i}</a>
            </c:forEach>
            <c:if test="${currentPage < totalPages}">
                <a href="film-list?page=${currentPage + 1}&searchQuery=${param.searchQuery}&searchType=${param.searchType}" class="btn btn-primary">→</a>
                <a href="film-list?page=${totalPages}&searchQuery=${param.searchQuery}&searchType=${param.searchType}" class="btn btn-info">Last Page</a>
            </c:if>
        </div>
    </c:if>
</div>
</div>

<%@ include file="/WEB-INF/views/components/film-list/film-add-modal.jsp" %>

<%@ include file="/WEB-INF/views/components/film-list/film-edit-modal.jsp" %>

<%@ include file="/WEB-INF/views/components/film-list/film-delete-modal.jsp" %>

<%@ include file="/WEB-INF/views/shared/footer.jsp" %>
<script src="https://code.jquery.com/jquery-3.5.1.min.js"></script>
<script src="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"></script>
<script src="${pageContext.request.contextPath}/js/film-list.js"></script>
<div class="modal-backdrop" id="modalBackdrop"></div>
</body>
</html>
