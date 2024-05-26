// Current page counter initialization for pagination.
var currentPage = 1;

// Set up a load more button and event listener to fetch additional films.
var loadMoreButton = document.querySelector(".load-more-button");

if (loadMoreButton && !loadMoreButton.hasEventListener("click", loadMoreFilms)) { // Check if the event listener is already set.
	loadMoreButton.addEventListener("click", loadMoreFilms); // Add the event listener to the load more button.
}

// Function to hide the load more button when no more films are available to load.
function hideLoadMoreButton() { // Hide the load more button when no more films are available.
	var loadMoreButton = document.querySelector(".load-more-button"); // Get the load more button element.
	if (loadMoreButton) { // Check if the load more button exists.
		loadMoreButton.style.display = 'none'; // Hide the load more button.
	}
}

// Function to show the load more button when additional films are available for loading.
function showLoadMoreButton() { // Show the load more button when additional films are available.
	var loadMoreButton = document.querySelector(".load-more-button"); // Get the load more button element.
	if (loadMoreButton) {
		loadMoreButton.style.display = 'block';
	}
}

// Function for dynamically creating film cards and adding them to the DOM.
function addFilmEventListeners(films) {
	var container = document.getElementById('film-container');

	// Iterate through each film object in the array to create its DOM element.
	films.forEach(film => {
		var filmCard = document.createElement('div'); // Create a new div element for the film card.
		filmCard.className = 'film-card'; // Set the class name for the film card.
		filmCard.setAttribute('data-trailer', film.trailerKey); // Set the data attribute for the film card.
		filmCard.innerHTML = `
            <img src='${film.poster}' alt='${film.title} Poster' class='filmPoster'/>
            <div class='film-details'>
                <h4>${film.title}</h4>
                <p>Year: ${film.year}</p>
                <p>Director: ${film.director}</p>
                <p>Stars: ${film.stars}</p>
            </div>
            <div class='playButton'>▶</div>`; // Set the inner HTML for the film card.

		// Append the newly created card to the container, and set up a click event listener for each card.
		container.appendChild(filmCard); // Append the film card to the container.
		filmCard.addEventListener('click', function() { // Add a click event listener to the film card.

			var modal = document.getElementById('trailerModal'); // Get the trailer modal element.
			var iframe = document.getElementById('trailerIframe'); // Get the trailer iframe element.

			// Extract trailer key and load the corresponding YouTube video in an iframe.
			var trailerKey = filmCard.getAttribute('data-trailer');
			if (trailerKey) { // Check if the trailer key is available.
				var trailerURL = 'https://www.youtube.com/embed/' + trailerKey + '?autoplay=1'; // Construct the trailer URL.
				iframe.src = trailerURL; // Set the iframe source to the trailer URL.
				modal.style.display = 'flex'; // Display the modal.
			}
		});
	});
}

// Function to display a loading animation when data is being fetched.
function showLoading() {
	var overlay = document.getElementById("loadingOverlay");
	overlay.style.display = "flex";
}

// Function to hide the loading animation when data fetching is complete.
function hideLoading() {
	var overlay = document.getElementById("loadingOverlay");
	overlay.style.display = "none";
}

// Function to load more films from the server by making an AJAX request to a specific endpoint.
function loadMoreFilms() {
	var xhr = new XMLHttpRequest();
	var contextPath = document.querySelector('#film-container').getAttribute('data-context-path');
	xhr.open("GET", contextPath + "/film-cards?page=" + currentPage, true);

	xhr.onload = function() { // Process the response and add the film cards to the DOM.
		if (xhr.status === 200) { // Check if the response status is OK.
			var films = JSON.parse(xhr.responseText); // Parse the JSON response.
			addFilmEventListeners(films); // Process and display additional films.

			currentPage++; // Increment the page count to fetch next set of films next time.
			hideLoading(); // Hide the loading animation.
		} else {
			console.error("Error loading films."); // Log an error message if the response status is not OK.
			hideLoading(); // Hide the loading animation.
		}
	};

	xhr.onerror = function() { // Log an error message if an error occurs during the XMLHttpRequest.
		console.error("An error occurred during the XMLHttpRequest."); // Log an error message.
		hideLoading(); // Hide the loading animation.
	};

	showLoading(); // Show loading animation while films are being fetched.
	xhr.send();
}


document.querySelector(".load-more-button").addEventListener("click", loadMoreFilms); // Add event listener to load more button.

// Function to refresh the content area with new or updated film data.
function refreshContent() {
	var container = document.getElementById('film-container'); // Get the film container element.
	var contextPath = container.getAttribute('data-context-path'); // Get the context path from the container's data attribute.
	var xhr = new XMLHttpRequest(); // Create a new XMLHttpRequest object.

	xhr.open("GET", contextPath + "/film-cards?page=1", true); // Fetch the first page of films.

	xhr.onload = function() { // Process the response and add the film cards to the DOM.
		if (xhr.status === 200) { // Check if the response status is OK.
			var films = JSON.parse(xhr.responseText); // Parse the JSON response.
			container.innerHTML = ""; // Clear the existing film cards.
			addFilmEventListeners(films); // Add the new film cards.
		} else {
			console.error("Error refreshing content: " + xhr.statusText); // Log an error message if the response status is not OK.
		}
	};

	xhr.onerror = function() { // Log an error message if an error occurs during the XMLHttpRequest.
		console.error("Network Error"); // Log a network error message.
	};

	xhr.send(); // Send the XMLHttpRequest to fetch the films.
}

var debounceTimer; // Timer for debouncing search input.

// Function to search for films based on user input.
function searchFilms() {
	var input = document.getElementById("film-search-input"); // Get the search input field.
	var query = input.value.trim(); // Get the trimmed search query.
	var container = document.getElementById('film-container'); // Get the film container element.

	if (query === "") { // Check if the search query is empty.
		showLoadMoreButton(); // Show the load more button.

		refreshContent(); // Refresh the content area with the original film data.
		return; // Exit the function.
	} else { // Hide the load more button if the search query is not empty.
		hideLoadMoreButton(); // Hide the load more button.
	}

	clearTimeout(debounceTimer); // Clear the debounce timer to prevent rapid firing of the function.

	debounceTimer = setTimeout(function() { // Set a new debounce timer to delay the search function.
		if (input.value.trim() === "") { // Check if the search input is empty.
			return; // Exit the function.
		}
		showLoading(); // Show the loading animation while fetching search results.
		fetchFilms(query, container); // Fetch films based on the search query.
	}, 500); // Set the debounce timer to 500 milliseconds.
}

// Function to fetch films from the server based on search query.
function fetchFilms(query, container) { // Fetch films based on the search query.
	var xhr = new XMLHttpRequest(); // Create a new XMLHttpRequest object.
	var contextPath = container.getAttribute('data-context-path'); // Get the context path from the container's data attribute.
	xhr.open("GET", contextPath + "/search-films?query=" + encodeURIComponent(query), true); // Fetch films based on the search query.

	xhr.onload = function() { // Process the response and add the film cards to the DOM.
		hideLoading();
		if (xhr.status === 200) { // Check if the response status is OK.
			var films = JSON.parse(xhr.responseText); // Parse the JSON response.
			container.innerHTML = ""; // Clear the existing film cards.
			addFilmEventListeners(films); // Add the new film cards.
		} else {
			console.error("Error retrieving search results."); // Log an error message if the response status is not OK.
		}
	};
	xhr.send(); // Send the XMLHttpRequest to fetch the films.
}

// Event listener for the search input field.
document.getElementById("film-search-input").addEventListener('input', function() {
	searchFilms(); // Search for films based on user input.
});

// Event listener for the search form.
document.addEventListener('DOMContentLoaded', function() { // Load more films when the page is loaded.
	loadMoreFilms();

	var modal = document.getElementById('trailerModal'); // Get the trailer modal element.
	var iframe = document.getElementById('trailerIframe'); // Get the trailer iframe element.
	var closeButton = document.querySelector('.close'); // Get the close button element.

	closeButton.addEventListener('click', function() { // Close the modal when the close button is clicked.
		iframe.src = ''; // Clear the iframe source.
		modal.style.display = 'none'; // Hide the modal.
	});

	window.addEventListener('click', function(event) { // Close the modal when the user clicks outside the modal.
		if (event.target === modal) { // Check if the click target is the modal.
			iframe.src = ''; // Clear the iframe source.
			modal.style.display = 'none'; // Hide the modal.
		}
	});

	document.addEventListener('keydown', function(event) { // Close the modal when the user presses the escape key.
		if (event.key === 'Escape') { // Check if the key pressed is the escape
			iframe.src = ''; // Clear the iframe source.
			modal.style.display = 'none'; // Hide the modal.
		}
	});
});