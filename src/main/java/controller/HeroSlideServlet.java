package controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import dao.FilmDAO;
import dao.TMDbApiDAO;
import model.Film;

/**
 * Servlet to handle requests for dynamically generating a hero slide on the front-end.
 * It interacts with a local database and an external API to enrich film data with additional media metadata.
 */
@WebServlet(name = "HeroSlideServlet", urlPatterns = { "/hero-slide" })
public class HeroSlideServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * Responds to GET requests by fetching film data, augmenting it with external API data,
     * and forwarding the enriched data to the JSP for display.
     *
     * @param request  The HttpServletRequest object that contains the client request
     * @param response The HttpServletResponse object that contains the server's response
     * @throws ServletException if the request could not be handled
     * @throws IOException if an input or output error is detected when the servlet handles the request
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        FilmDAO filmDAO = new FilmDAO();
        TMDbApiDAO tmdbApiDAO = new TMDbApiDAO();

        try {
            // Fetch all films from the database and shuffle to randomise the display order
            List<Film> allFilms = filmDAO.getAllFilms();
            Collections.shuffle(allFilms);
            List<Film> validFilms = new ArrayList<>();

            // Processing each film to ascertain if it meets criteria for display on the hero slide
            for (Film film : allFilms) {
                if (validFilms.size() >= 10) {
                    break; // Limit the hero slide to a maximum of 10 films
                }

                // Fetch external API data using the film's title and its release year
                JSONObject movieData = tmdbApiDAO.fetchMovieData(film.getTitle(), film.getYear());
                if (movieData == null) {
                    continue;
                }

                // Extract the unique movie ID from the fetched data
                String movieId = tmdbApiDAO.getMovieId(movieData);
                if (movieId == null) {
                    continue;
                }

                // Fetch detailed movie data including media assets (videos and backdrops)
                JSONObject movieDetails = tmdbApiDAO.fetchMovieDetails(movieId);
                if (movieDetails == null) {
                    continue;
                }

                // Verify movieDetails has essential keys for the hero slide
                if (movieDetails.has("backdrop_path") && movieDetails.has("videos")
                        && movieDetails.getJSONObject("videos").has("results")) {
                    JSONObject videos = movieDetails.getJSONObject("videos");
                    String trailerKey = tmdbApiDAO.extractTrailerKey(videos);
                    if (trailerKey != null) {
                        // Store enriched film details and add to the list of films to be displayed
                        film.setBackdrop("https://image.tmdb.org/t/p/original" + movieDetails.optString("backdrop_path"));
                        film.setPoster("https://image.tmdb.org/t/p/w500" + movieDetails.optString("poster_path"));
                        film.setTrailerKey(trailerKey);
                        validFilms.add(film);
                    }
                }
            }

            // Set the enriched film list as a request attribute and forward to the view component
            request.setAttribute("films", validFilms);
            request.getRequestDispatcher("/WEB-INF/views/components/hero-slide.jsp").forward(request, response);
        } catch (Exception e) {
            // Log the exception and send a server error response
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                    "Server error while fetching films: " + e.getMessage());
        }
    }
}