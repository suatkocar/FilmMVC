<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>FilmMVC</title>
<link rel="icon" href="${pageContext.request.contextPath}/images/favicon.ico" type="image/x-icon">
<!-- Google Fonts -->
<link rel="preconnect" href="https://fonts.googleapis.com">
<link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
<link
	href="https://fonts.googleapis.com/css2?family=Alegreya:ital@0;1&family=Cabin:ital,wght@0,400..700;1,400..700&family=Fira+Sans:ital,wght@0,100;0,200;0,300;0,400;0,500;0,600;0,700;0,800;0,900;1,100;1,200;1,300;1,400;1,500;1,600;1,700;1,800;1,900&family=Lexend&display=swap"
	rel="stylesheet">
<!-- Google Fonts -->
<link rel="stylesheet"
	href="https://unpkg.com/swiper/swiper-bundle.min.css" />
	<link rel="stylesheet"
	href="${pageContext.request.contextPath}/css/index.css">
<script>
	var contextPath = "${pageContext.request.contextPath}";
</script>
</head>
<body class="body-background">
	<div class="main-content">
	<%@ include file="/WEB-INF/views/shared/header.jsp"%>
	<%@ include file="/WEB-INF/views/components/home/hero-slide.jsp"%>
	<%@ include file="/WEB-INF/views/components/home/search-box.jsp"%>
	<%@ include file="/WEB-INF/views/components/home/film-cards.jsp"%>
	<%@ include file="/WEB-INF/views/shared/footer.jsp"%>
	</div>
	<script src="https://unpkg.com/swiper/swiper-bundle.min.js"></script>
	<script src="${pageContext.request.contextPath}/js/film-cards.js"></script>
	<script>
		document.addEventListener('DOMContentLoaded', function() {
			loadMoreFilms();
		});
	</script>
	</body>
	</html>