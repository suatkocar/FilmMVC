<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

    <meta charset="UTF-8">
    <title>Header</title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/header.css">
    <script src="https://cdn.jsdelivr.net/npm/lottie-web/build/player/lottie.min.js"></script>

    <div class="header" id="header"> 
        <div class="headerWrap">
            <div id="header-logo-container" class="header-content-logo"></div>
            <audio id="headerMusic" src="${pageContext.request.contextPath}/audio/The-Godfather-Theme-Song.mp3"></audio>
            <ul class="headerNav">
                <li class="navItem">
                    <a href="${pageContext.request.contextPath}/index">Home</a>
                </li>
                <li class="navItem">
                    <a href="${pageContext.request.contextPath}/film-list">Film List</a>
                </li>
            </ul>
        </div>
    </div>
    
<script>
document.addEventListener('DOMContentLoaded', function() {
    const headerLogo = document.getElementById('header-logo-container');
    const headerMusic = document.getElementById('headerMusic');
    let isPlaying = false;
    
    let logoAnimation = lottie.loadAnimation({
        container: headerLogo,
        renderer: 'svg',
        loop: true,
        autoplay: true,
        path: '${pageContext.request.contextPath}/json/header-logo-paused.json'
    });

    headerLogo.addEventListener('click', function() {
        if (!isPlaying) {
            headerMusic.play();
            logoAnimation.destroy();
            logoAnimation = lottie.loadAnimation({
                container: headerLogo,
                renderer: 'svg',
                autoplay: true,
                path: '${pageContext.request.contextPath}/json/header-logo-playing.json'
            });
            headerLogo.classList.add('header-logo-playing');
            headerLogo.classList.remove('header-logo-paused');
            isPlaying = true;
        } else {
            headerMusic.pause();
            logoAnimation.destroy();
            logoAnimation = lottie.loadAnimation({
                container: headerLogo,
                renderer: 'svg',
                autoplay: true,
                path: '${pageContext.request.contextPath}/json/header-logo-paused.json'
            });
            headerLogo.classList.add('header-logo-paused');
            headerLogo.classList.remove('header-logo-playing');
            isPlaying = false;
        }
    });
});
</script>
    
    <script>
window.addEventListener("scroll", shrinkHeader);
function shrinkHeader() {
    var header = document.getElementById("header");
    var scrollPosition = window.pageYOffset || document.documentElement.scrollTop;
    var isShrunk = scrollPosition > 100;
    if (isShrunk) {
        header.classList.add("shrink");
    } else {
        header.classList.remove("shrink");
    }
}
</script>
