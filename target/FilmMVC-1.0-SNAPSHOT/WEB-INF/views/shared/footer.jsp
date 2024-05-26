<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Footer</title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/footer.css">
</head>
<body>
    <div class="footer"> 
        <div class="footer__content">
            <div id="footer-logo" class="footer-content-logo"></div>
            <div class="footer__copyright">
                &copy; <%= java.time.LocalDate.now().getYear() %> - Designed by Suat Kocar
            </div>
        </div>
    </div>
    
<script>
document.addEventListener('DOMContentLoaded', function() {
  
    var animation = bodymovin.loadAnimation({
        container: document.getElementById("footer-logo"), 
        renderer: "svg", 
        loop: false,
        autoplay: true,
        path: "${pageContext.request.contextPath}/json/footer-logo.json", 
    });

    
    animation.addEventListener('DOMLoaded', function() {
        
        setTimeout(function() {
        
            animation.goToAndStop(100, true);
        }, 3000);
    });
});
</script>

    <script src="https://cdn.jsdelivr.net/npm/lottie-web/build/player/lottie.min.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/bodymovin/5.12.2/lottie.min.js"></script>
</body>
</html>
