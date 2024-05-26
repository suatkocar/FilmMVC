package controller;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dao.FilmDAO;
import dao.TMDbApiDAO;
import model.Film;

/**
 * Servlet to handle requests for the main index page of the application.
 * Retrieves film data and prepares it for display.
 */
@WebServlet(name = "IndexServlet", urlPatterns = { "/index" })
public class IndexServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * Responds to GET requests by fetching film data, randomising it, processing for additional details,
     * and then forwarding that data to the JSP for the index page.
     *
     * @param request  The HttpServletRequest object containing client-specific data including parameters
     * @param response The HttpServletResponse object that contains the response the servlet sends to the client
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O related error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        FilmDAO filmDAO = new FilmDAO(); // Instantiates a new object for accessing database operations related to films
        TMDbApiDAO tmdbApiDAO = new TMDbApiDAO(); // Instantiates a new object for accessing external API for film details
        List<Film> allFilms = filmDAO.getAllFilms(); // Retrieves all films from the database
        Collections.shuffle(allFilms); // Randomises the order of films to provide varied content on each visit

        List<Film> films = tmdbApiDAO.getProcessedFilms(allFilms); // Processes all films through an external API to obtain additional details

        request.setAttribute("films", films); // Sets the list of processed films as an attribute to the request object
        request.getRequestDispatcher("/index.jsp").forward(request, response); // Forwards request to the index.jsp page
    }
}