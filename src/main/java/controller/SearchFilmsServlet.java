package controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import com.google.gson.Gson;

import dao.FilmDAO;
import dao.TMDbApiDAO;
import model.Film;

/** 
 * Servlet responsible for handling film searches and returning the results in JSON format.
 */
@WebServlet("/search-films")
public class SearchFilmsServlet extends HttpServlet {
   
    private static final long serialVersionUID = 1L;

	/**
     * Responds to GET requests by performing a film search and augmenting film data with 
     * additional details fetched from an external API (TMDb).
     * 
     * @param request  HttpServletRequest from the client, which includes search parameters.
     * @param response HttpServletResponse to the client, containing search results in JSON format.
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs during processing
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        String searchQuery = request.getParameter("query"); // Retrieves the search query from request parameters.
        FilmDAO filmDAO = new FilmDAO();
        TMDbApiDAO tmdbApiDAO = new TMDbApiDAO();
        List<Film> films = filmDAO.searchFilms(searchQuery); // Search films locally that match the query.

        // Enhance each film with data from external API
        for (Film film : films) {
            try {
                JSONObject movieData = tmdbApiDAO.fetchMovieData(film.getTitle(), film.getYear());
                if (movieData != null) {
                    // Set the poster URL from the movie data retrieved or use a placeholder if not available
                    String posterPath = movieData.optString("poster_path");
                    film.setPoster(posterPath != null ? "https://image.tmdb.org/t/p/w500" + posterPath
                            : "https://via.placeholder.com/500");

                    // Get movie ID and fetch detailed movie information
                    String movieId = tmdbApiDAO.getMovieId(movieData);
                    if (movieId != null) {
                        JSONObject movieDetails = tmdbApiDAO.fetchMovieDetails(movieId);
                        String trailerKey = tmdbApiDAO.extractTrailerKey(movieDetails.optJSONObject("videos"));
                        film.setTrailerKey(trailerKey); // Set trailer key if available
                    }
                }
            } catch (Exception e) {
                System.err.println("Error fetching movie data: " + e.getMessage()); // Log error and continue processing other films
            }
        }

        // Convert the film list with enhanced details to JSON and write it to the response
        String json = new Gson().toJson(films);
        response.getWriter().write(json);
    }
}