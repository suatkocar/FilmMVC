package controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

import dao.FilmDAO;
import dao.TMDbApiDAO;
import model.Film;

// Servlet mapping for this class to handle URL pattern "/film-cards".
@WebServlet("/film-cards")
public class FilmCardsServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	// Handles GET requests to provide enhanced film details including trailers.
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// Sets the content type of the response to JSON format.
		response.setContentType("application/json");
		// Sets the character encoding of the response to UTF-8 to support special characters.
		response.setCharacterEncoding("UTF-8");

		// Default page number and size are initialised.
		int page = 1;
		int size = 24; 
		// Try and catch block; to parse the 'page' parameter, and will not change the value if parsing fails.
		try {
			page = Integer.parseInt(request.getParameter("page"));
		} catch (NumberFormatException e) {
			// Prints stack trace for debugging if there is an issue with parsing the page number.
			e.printStackTrace();
		}

		FilmDAO filmDAO = new FilmDAO();
		TMDbApiDAO tmdbApiDAO = new TMDbApiDAO();

		List<Film> films = null;
		// With try and catch block; Attempting to fetch a paginated list of films from the database.
		try {
			films = filmDAO.getFilmsPaginated(page, size);
		} catch (Exception e) {
			// Prints stack trace for debugging if there is an issue with film retrieval.
			e.printStackTrace();
		}
		List<Film> filmsWithTrailers = new ArrayList<>();

		// Processing each film and attempt to augment with external API data.
		for (Film film : films) {
			try {
				var movieData = tmdbApiDAO.fetchMovieData(film.getTitle(), film.getYear());
				if (movieData != null) {
					var posterPath = movieData.optString("poster_path");
					if (posterPath != null) {
						// Constructs full URL for the film poster.
						posterPath = "https://image.tmdb.org/t/p/w500" + posterPath;
						film.setPoster(posterPath);
					}

					String movieId = tmdbApiDAO.getMovieId(movieData);
					if (movieId != null) {
						var movieDetails = tmdbApiDAO.fetchMovieDetails(movieId);
						if (movieDetails != null && movieDetails.has("videos")) {
							String trailerKey = tmdbApiDAO.extractTrailerKey(movieDetails.getJSONObject("videos"));
							if (trailerKey != null) {
								// Sets the trailer key if it exists.
								film.setTrailerKey(trailerKey);
							}
						}
					}
				}
				filmsWithTrailers.add(film);
			} catch (Exception e) {
				// Logs error and assigns a placeholder image if fetching movie data fails.
				System.err.println("Error fetching movie data: " + e.getMessage());
				film.setPoster("https://via.placeholder.com/500");
				filmsWithTrailers.add(film);
			}
		}

		// Converts the film list with trailers into JSON and sends it as the response.
		String json = new Gson().toJson(filmsWithTrailers);
		response.getWriter().write(json);
	}
}