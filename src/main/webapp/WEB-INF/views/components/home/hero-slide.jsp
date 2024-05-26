<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/hero-slide.css">
<script src="${pageContext.request.contextPath}/js/hero-slide.js"></script>
<link rel="icon" href="${pageContext.request.contextPath}/images/favicon.ico" type="image/x-icon">
<!-- Google Fonts -->
<link rel="preconnect" href="https://fonts.googleapis.com">
<link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
<link
	href="https://fonts.googleapis.com/css2?family=Alegreya:ital@0;1&family=Cabin:ital,wght@0,400..700;1,400..700&family=Fira+Sans:ital,wght@0,100;0,200;0,300;0,400;0,500;0,600;0,700;0,800;0,900;1,100;1,200;1,300;1,400;1,500;1,600;1,700;1,800;1,900&family=Lexend&display=swap"
	rel="stylesheet">
<!-- Google Fonts -->

<div class="hero-slide">
    <div class="swiper-wrapper">
        <c:forEach items="${films}" var="film">
            <div class="swiper-slide heroItem" style="background-image: url('${film.backdrop}')">
                <div class="posterSection">
                    <img src="${film.poster}" alt="{film.title} Poster" class="heroSlidefilmPoster"/>
                </div>
                <div class="infoSection">
                    <h2 class="filmTitle">${film.title}</h2>
                    <p class="filmReview">${film.review}</p>
                  <button class="WatchTrailerButton open-btn" data-trailer="${film.trailerKey}">Watch Trailer</button>
                </div>
            </div>
        </c:forEach>
    </div>

</div>

<div id="trailerModal" class="modal">
    <div class="modal-content">
        <span class="close"></span>
        <iframe id="trailerIframe" width="1280px" height="720px" src="" frameborder="0" allowfullscreen></iframe>
    </div>
</div>
