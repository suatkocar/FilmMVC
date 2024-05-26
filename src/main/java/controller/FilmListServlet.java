package controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dao.FilmDAO;
import model.Film;

/**
 * Servlet implementation for listing films.
 * Supports both search and pagination features.
 */
@WebServlet("/film-list")
public class FilmListServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
	private FilmDAO filmDAO = new FilmDAO(); // DAO instance for database operations.
    private static final int PAGE_SIZE = 24; // Number of films to display per page.

    /**
     * Handles GET requests to fetch film data, either through search or pagination.
     * 
     * @param request The Http request containing potential query parameters.
     * @param response The Http response used for forwarding or sending errors.
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String searchQuery = request.getParameter("searchQuery"); // Retrieves the search query from request.
        String searchType = request.getParameter("searchType"); // Retrieves the type of search from request.
        String pageParam = request.getParameter("page"); // Retrieves current page number from request.
        int currentPage = (pageParam != null && !pageParam.isEmpty()) ? Integer.parseInt(pageParam) : 1; // Parses the current page or default to 1.
        List<Film> films = null; // List of films to be populated based on search or pagination.
        int totalFilms = 0; // Total number of films found.

        if (searchQuery != null && !searchQuery.trim().isEmpty()) {
            films = filmDAO.searchFilms(searchQuery, searchType); // Searches films based on query and type.
            totalFilms = films.size(); // Updates total films count based on search results.
            request.setAttribute("filmCount", totalFilms); // Sets film count attribute for the view.
            request.setAttribute("isSearch", true); // Indicates that this is search result.
        } else {
            try {
                films = filmDAO.getFilmsPaginated(currentPage, PAGE_SIZE); // Fetches paginated list of films.
                totalFilms = filmDAO.getTotalFilmCount(); // Gets total number of films in database.
                int totalPages = (int) Math.ceil((double) totalFilms / PAGE_SIZE); // Calculates total pages needed.
                request.setAttribute("totalPages", totalPages); // Sets total pages attribute for pagination control.
                request.setAttribute("isSearch", false);
            } catch (Exception e) {
                e.printStackTrace();
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Database access error."); // Sends an HTTP error response if database access fails.
                return;
            }
        }
        
        int totalPages = (int) Math.ceil((double) totalFilms / PAGE_SIZE); // Recalculates total pages as it might be different after the search.
        int window = 5; // Define page index window for pagination links.
        int startPage = Math.max(1, currentPage - window); // Calculates the start page for pagination.
        int endPage = Math.min(startPage + 2 * window, totalPages); // Calculates the end page for pagination.

        // Setting request attributes for page navigation and viewing.
        request.setAttribute("films", films); // Sets the films list attribute for the view.
        request.setAttribute("currentPage", currentPage); // Sets current page attribute.
        request.setAttribute("searchType", searchType); // Sets search type attribute.
        request.setAttribute("filmCount", totalFilms); // Sets total film count attribute.
        request.setAttribute("totalPages", totalPages); // Sets total pages attribute.
        request.setAttribute("startPage", startPage); // Sets start page attribute.
        request.setAttribute("endPage", endPage); // Sets end page attribute.

        // Forwarding request to JSP page for rendering film list.
        request.getRequestDispatcher("/WEB-INF/views/pages/film-list.jsp").forward(request, response);
    }
}