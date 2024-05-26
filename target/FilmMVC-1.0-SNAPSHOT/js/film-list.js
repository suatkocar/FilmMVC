// Listening for when the document content has fully loaded.
document.addEventListener('DOMContentLoaded', function() {
	// Select all elements with the class 'filmPoster'.
    const posters = document.querySelectorAll('.filmPoster'); // Returns a NodeList of elements.
    const modalBackdrop = document.getElementById('modalBackdrop'); // Returns the modal backdrop element.

    // Loop through each element in the NodeList.
    posters.forEach(poster => { // poster is the current element in the NodeList.
        poster.addEventListener('click', function() { // Add a click event listener to the current element.
            const clone = poster.cloneNode(true); // Clone the current element.
            clone.style.position = 'fixed'; // Set the position of the clone to fixed.
            clone.style.left = `${poster.getBoundingClientRect().left}px`; // Set the left position of the clone to the left position of the current element.
            clone.style.top = `${poster.getBoundingClientRect().top}px`; // Set the top position of the clone to the top position of the current element.
            clone.style.width = `${poster.offsetWidth}px`; // Set the width of the clone to the width of the current element.
            clone.style.height = `${poster.offsetHeight}px`; // Set the height of the clone to the height of the current element.
            clone.style.margin = 0; // Set the margin of the clone to 0.
            clone.style.transition = 'transform 0.5s ease-in-out'; // Set the transition of the clone.
            clone.style.pointerEvents = 'none'; // Set the pointer events of the clone to none.
            document.body.appendChild(clone); // Append the clone to the body.

            const centerX = (window.innerWidth / 2) - (poster.getBoundingClientRect().left + poster.offsetWidth * 3 / 2) - 35; // Calculate the center x position of the clone.
            const centerY = (window.innerHeight / 2) - (poster.getBoundingClientRect().top + poster.offsetHeight * 3 / 2) - 35; // Calculate the center y position of the clone.
            clone.style.transform = `translate(${centerX}px, ${centerY}px) scale(1.25)`; // Transform the clone to the center of the screen.

            clone.classList.add('expandedPoster'); // Add the class 'expandedPoster' to the clone.
            modalBackdrop.style.display = 'flex'; // Set the display of the modal backdrop to flex.

            modalBackdrop.onclick = function() { // Add a click event listener to the modal backdrop.
                clone.style.transform = ''; // Reset the transform of the clone.
                setTimeout(() => { // Set a timeout function.
                    document.body.removeChild(clone); // Remove the clone from the body.
                    modalBackdrop.style.display = 'none'; // Set the display of the modal backdrop to none.
                }, 500); // Set the timeout to 500 milliseconds.
            };

            window.addEventListener('keydown', function(event) { // Add a keydown event listener to the window.
                if (event.key === "Escape") { // Check if the key pressed is the escape key.
                    modalBackdrop.onclick(); // Call the click event listener on the modal backdrop.
                }
            }, { once: true }); // Set the event listener to only run once.
        });
    });
});

// Listening for when the document content has fully loaded.
function showLoading() {
    document.getElementById("loadingOverlay").style.display = "flex";
}

// Listening for when the document content has fully loaded.
function hideLoading() {
    document.getElementById("loadingOverlay").style.display = "none";
}

// Listening for when the document content has fully loaded.
document.addEventListener('DOMContentLoaded', function() {
    showLoading(); // Call the showLoading function.

    const form = document.querySelector('.search-form'); // Select the form element with the class 'search-form'.
    form.addEventListener('submit', function(event) {
        showLoading(); // Call the showLoading function.
    });

    const paginationLinks = document.querySelectorAll('.pagination a'); // Select all elements with the class 'pagination' and 'a'.
    paginationLinks.forEach(link => { // Loop through each element in the NodeList.
        link.addEventListener('click', function(event) { // Add a click event listener to the current element.
            showLoading(); // Call the showLoading function.
        });
    });

// Listening for when the document content has fully loaded.
    const clearSearchButton = document.querySelector('.clear-search'); // Select the element with the class 'clear-search'.
    clearSearchButton.addEventListener('click', function() { // Add a click event listener to the element.
        showLoading(); // Call the showLoading function.
    });
    setTimeout(hideLoading, 100); // Call the hideLoading function after 100 milliseconds.
});