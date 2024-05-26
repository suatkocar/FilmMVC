package controller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dao.FilmDAO;
import model.Film;

// Servlet mapping configuration: maps this servlet to the URL pattern '/FilmAdd'.
@WebServlet("/FilmAdd")
public class FilmAddServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
	// Instance of FilmDAO to handle database operations related to 'Film' data.
    private FilmDAO filmDAO = new FilmDAO();

    /**
     * The doPost method handles POST requests from clients, enabling film data submission.
     * 
     * @param request  The HttpServletRequest object that contains the client's request.
     * @param response The HttpServletResponse object that contains the servlet's response.
     * @throws ServletException if a servlet-specific error occurs.
     * @throws IOException if an I/O error occurs.
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Parameters are retrieved from the request and local variables are initialised.
        String title = request.getParameter("title");
        int year = Integer.parseInt(request.getParameter("year"));
        String director = request.getParameter("director");
        String stars = request.getParameter("stars");
        String review = request.getParameter("review");
        
        // A new Film object is created with the parameters received from the request.
        Film newFilm = new Film(0, title, year, director, stars, review);
        // The new film is inserted into the database using the FilmDAO.
        filmDAO.insertFilm(newFilm);

        // After inserting the film, redirect the client to the 'film-list' URL.
        response.sendRedirect("film-list");
    }
}