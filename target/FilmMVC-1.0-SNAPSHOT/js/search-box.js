function searchFilms() {
	// Get the base URL of the application from a 'data-context-path' attribute of the film container.
    var contextPath = document.querySelector('#film-container').getAttribute('data-context-path');
    var input = document.getElementById("film-search-input"); // Get the input element.
    var query = input.value.trim(); // Get the value of the input and remove leading and trailing white spaces.
    var container = document.getElementById("film-container"); // Get the container element.

    if (query === "") { // If the query is empty, load more films.
        container.innerHTML = ""; // Clear the container.
        loadMoreFilms(); // Load more films.
        return; // Exit the function.
    }

    var xhr = new XMLHttpRequest(); // Create a new XMLHttpRequest object.
    xhr.open("GET", contextPath + "/search-films?query=" + encodeURIComponent(query), true); // Initialize the request.

    xhr.onload = function() { // Define the onload event handler.
        if (xhr.status === 200) { // If the request was successful,
            var films = JSON.parse(xhr.responseText); // Parse the response text as JSON.

            container.innerHTML = ""; // Clear the container.

            films.forEach(film => { // Iterate over the films.
                var filmCard = document.createElement("div"); // Create a new div element.
                filmCard.className = "film-card";
                filmCard.innerHTML = `
                    <img src='${film.poster}' alt='${film.title} Poster' class='filmPoster'/>
                    <div class='film-details'>
                        <h4>${film.title}</h4>
                        <p>Year: ${film.year}</p>
                        <p>Director: ${film.director}</p>
                        <p>Stars: ${film.stars}</p>
                    </div>
                    <div class='playButton'>►</div>
                `;
                container.appendChild(filmCard); // Append the film card to the container.
            });
        } else { // If the request was not successful,
            console.error("Error retrieving search results.");
        }
    };

    xhr.send(); // Send the request.
}