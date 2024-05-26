package controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dao.FilmDAO;
import model.Film;

// Servlet mapping for handling URL access to "/FilmDelete".
@WebServlet("/FilmDelete")
public class FilmDeleteServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
	// Instance of FilmDAO is used for database operations concerning films.
	private FilmDAO filmDAO = new FilmDAO();

    /**
     * Handles GET requests to fetch a film for deletion confirmation.
     *
     * @param request  The HttpServletRequest object that contains the client's request.
     * @param response The HttpServletResponse object that contains the servlet's response.
     * @throws ServletException if a servlet-specific error occurs.
     * @throws IOException if an I/O error occurs.
     */
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// Retrieves the film ID from the request parameters.
		int id = Integer.parseInt(request.getParameter("id"));
		// Fetches the film based on the ID provided.
		Film film = filmDAO.getFilmByID(id);

		// If the film exists, sets it in the request and forwards to the delete page.
		if (film != null) {
			request.setAttribute("film", film);
			request.getRequestDispatcher("/WEB-INF/views/components/film-list/film-delete-modal.jsp").forward(request, response);
		} else {
			// Redirects to the film list page if the film does not exist.
			response.sendRedirect("film-list");
		}
	}

    /**
     * Handles POST requests to delete a specific film.
     *
     * @param request  The HttpServletRequest object that contains the client's request.
     * @param response The HttpServletResponse object that contains the servlet's response.
     * @throws ServletException if a servlet-specific error occurs.
     * @throws IOException if an I/O error occurs.
     */
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// Retrieves the film ID from the request parameters and deletes the film.
		int id = Integer.parseInt(request.getParameter("id"));
		filmDAO.deleteFilm(id);

		// Redirects to the film list page after the deletion.
		response.sendRedirect("film-list");
	}
}