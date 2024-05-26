package controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dao.FilmDAO;
import model.Film;

// Defines the servlet for handling edit operations on film records via the "/FilmEdit" URL pattern.
@WebServlet("/FilmEdit")
public class FilmEditServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
	// Instantiating the FilmDAO class for database operations pertaining to films.
    private FilmDAO filmDAO = new FilmDAO();

    /**
     * Handles the GET request to facilitate the editing of a film, fetching the details for display in the edit modal.
     *
     * @param request  The HttpServletRequest object that contains the client's request.
     * @param response The HttpServletResponse object that contains the servlet's response.
     * @throws ServletException if a servlet-specific error occurs.
     * @throws IOException if an I/O error occurs.
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Parsing the film id from the query parameters.
        int id = Integer.parseInt(request.getParameter("id"));
        // Retrieving the film from the database using FilmDAO based on the provided id.
        Film film = filmDAO.getFilmByID(id);

        // If the film is found, sets it as a request attribute and forwards to the edit modal view.
        if (film != null) {
            request.setAttribute("film", film);
            request.getRequestDispatcher("/WEB-INF/views/components/film-list/film-edit-modal.jsp").forward(request, response);
        } else {
			// Redirecting to the film list page if the film is not found.
			response.sendRedirect("film-list");
        }
    }
    
    /**
     * Handles the POST request to update the details of a specific film in the database after edits are submitted.
     *
     * @param request  The HttpServletRequest object that contains the client's request.
     * @param response The HttpServletResponse object that contains the servlet's response.
     * @throws ServletException if a servlet-specific error occurs.
     * @throws IOException if an I/O error occurs.
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Retrieving updated film attributes from the form submission.
        int id = Integer.parseInt(request.getParameter("id"));
        String title = request.getParameter("title");
        int year = Integer.parseInt(request.getParameter("year"));
        String director = request.getParameter("director");
        String stars = request.getParameter("stars");
        String review = request.getParameter("review");

        // Creating a new Film instance with the updated information.
        Film updatedFilm = new Film(id, title, year, director, stars, review);
        // Updating the film in the database using the FilmDAO class.
        filmDAO.updateFilm(updatedFilm);

        // Redirecting to the film list page, where the updated list will be displayed.
        response.sendRedirect("film-list");
    }
}