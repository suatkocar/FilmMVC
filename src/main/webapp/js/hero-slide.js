// Event listener for when the DOM content has fully loaded.
document.addEventListener('DOMContentLoaded', function() {
	// Initialize a Swiper instance for the hero slide widgets.
    var swiper = new Swiper('.hero-slide', {
        loop: true, // Infinite loop
        autoplay: {
            delay: 4000, // Autoplay interval of 4 seconds
            disableOnInteraction: false // Autoplay continues even when user interacts with the slider
        }
    });

// Select modal, close button, and iframe elements for trailer display.
    var modal = document.getElementById('trailerModal');
    var span = document.querySelector('.close');
    var iframe = document.getElementById('trailerIframe');

// Add click listeners to all buttons that open trailers.
    document.querySelectorAll('.open-btn').forEach(button => {
        button.addEventListener('click', openTrailer);
    });
    
// Listener for closing the modal.
    span.addEventListener('click', closeModal);
    
// Function to show the trailer in a modal and pause the swiper autoplay.
    function openTrailer() {
        var trailerKey = this.getAttribute('data-trailer'); // Get the trailer key from the data attribute
        iframe.src = `https://www.youtube.com/embed/${trailerKey}?autoplay=1&rel=0&showinfo=0&enablejsapi=1`; // Set the source of the iframe to the trailer URL
        modal.style.display = 'flex'; // Display the modal
        swiper.autoplay.stop(); // Pause the swiper autoplay
    }
    
// Function to close the modal and resume the swiper autoplay.
    function closeModal() {
        iframe.src = ''; // Clear the source of the iframe
        modal.style.display = 'none'; // Hide the modal
        swiper.autoplay.start(); // Resume the swiper autoplay
    }

    window.addEventListener('click', function(event) { // Close the modal when clicking outside of it
        if (event.target === modal) { // Check if the event target is the modal
            closeModal(); // Close the modal
        }
    });

    document.addEventListener('keydown', function(event) { // Close the modal when pressing the escape key
        if (event.key === "Escape") {
            closeModal();
        }
    });

// Add click listeners to all poster elements to expand them.
    var posters = document.querySelectorAll('.heroSlidefilmPoster');
    posters.forEach(poster => { // Iterate over each poster element
        poster.addEventListener('click', function(event) { // Add a click listener to the poster
            event.stopPropagation(); // Prevent the click event from bubbling up to the window
            if (!this.classList.contains('expandedPoster')) { // Check if the poster is not expanded
                posters.forEach(p => {
                    p.classList.remove('expandedPoster'); // Remove the expandedPoster class from all posters
                    p.style.cssText = ''; // Clear the inline styles of all posters
                });
                
// Calculate the center of the window and the center of the poster
                const rect = this.getBoundingClientRect(); // Get the bounding rectangle of the poster
                const winWidth = window.innerWidth / 2; // Get the width of the window
                const winHeight = window.innerHeight / 2; // Get the height of the window
                const centerX = winWidth - rect.left - rect.width/2; // Calculate the center of the window
                const centerY = winHeight - rect.top - rect.height/2; // Calculate the center of the poster
                this.dataset.initialTransform = `translate(0, 0) scale(1)`; // Save the initial transform of the poster
                this.style.transform = `translate(${centerX}px, ${centerY}px) scale(1.5)`; // Set the transform of the poster
                this.classList.add('expandedPoster'); // Add the expandedPoster class to the poster
                swiper.autoplay.stop(); // Pause the swiper autoplay
            }
        });
    });

// Add click listeners to the window and document to close the expanded poster.
    window.addEventListener('click', function() { // Close the expanded poster when clicking outside of it
        posters.forEach(poster => {
            if(poster.classList.contains('expandedPoster')) { // Check if the poster is expanded
                poster.classList.remove('expandedPoster'); // Remove the expandedPoster class from the poster
                poster.style.cssText = ''; // Clear the inline styles of the poster
                swiper.autoplay.start(); // Resume the swiper autoplay
            }
        });
    });

// Close the expanded poster when pressing the escape key.
    document.addEventListener('keydown', function(event) {
        if (event.key === "Escape") {
            posters.forEach(poster => { // Iterate over each poster element
                if (poster.classList.contains('expandedPoster')) { // Check if the poster is expanded
                    poster.classList.remove('expandedPoster'); // Remove the expandedPoster class from the poster
                    poster.style.transform = poster.dataset.initialTransform || ''; // Reset the transform of the poster
                    swiper.autoplay.start(); // Resume the swiper autoplay
                }
            });
        }
    });
});