package dao;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.json.JSONArray;
import org.json.JSONObject;

import model.Film;

/**
 * Data Access Object (DAO) class for accessing data of films stored in a MySQL database.
 * Uses JDBC for database connection and operations.
 */

public class FilmDAO {	
	private String jdbcUrl;
	private String jdbcUser;
	private String jdbcPassword;
	
	private TMDbApiDAO tmdbApiDAO = new TMDbApiDAO(); // TMDb API DAO object for fetching movie data.

    /**
     * Constructor to load database driver.
     */
	public FilmDAO() {
		try {
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Load properties file
            try (InputStream input = getClass().getClassLoader().getResourceAsStream("config.properties")) {
                Properties prop = new Properties();
                if (input == null) {
                    System.out.println("Sorry, unable to find config.properties");
                    return;
                }
                prop.load(input);
                this.jdbcUrl = prop.getProperty("jdbcUrl");
                this.jdbcUser = prop.getProperty("jdbcUser");
                this.jdbcPassword = prop.getProperty("jdbcPassword");
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
	
    /**
     * Creates and returns a connection to the database.
     * @return Connection to the MySQL database.
     * @throws SQLException if a database access error occurs.
     */
	private Connection getConnection() throws SQLException {
		return DriverManager.getConnection(jdbcUrl, jdbcUser, jdbcPassword);
	}

	/**
     * Closes the JDBC resources used in database operations.
     * @param conn Connection object to be closed.
     * @param pstmt PreparedStatement object to be closed.
     * @param rs ResultSet object to be closed.
     */
	private void closeResources(Connection conn, PreparedStatement pstmt, ResultSet rs) {
		try {
			if (rs != null) {
				rs.close();
			}
			if (pstmt != null) {
				pstmt.close();
			}
			if (conn != null) {
				conn.close();
			}
		} catch (SQLException e) {
			System.err.println("Error closing resources: " + e.getMessage());
		}
	}

    /**
     * Extracts and returns a Film object from the current row of the ResultSet.
     * @param rs ResultSet containing film data.
     * @return Film extracted from the current row of the ResultSet.
     * @throws SQLException if a database access error occurs.
     */
	private Film getNextFilm(ResultSet rs) throws SQLException {
		return new Film(rs.getInt("id"), rs.getString("title"), rs.getInt("year"), rs.getString("director"),
				rs.getString("stars"), rs.getString("review"));
	}

	/**
	 * Fetches all films from the database.
	 * @return List of Film objects containing all films from the database.
	 */
	public List<Film> getAllFilms() {
		System.out.println("Fetching all films from database");
		String sql = "SELECT * FROM films";
		List<Film> films = new ArrayList<>();
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			conn = getConnection();
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				films.add(getNextFilm(rs));
				System.out.println("Loaded film: " + rs.getString("title"));
			}
		} catch (SQLException e) {
			System.err.println("SQL Error: " + e.getMessage());
			e.printStackTrace();
		} finally {
			closeResources(conn, pstmt, rs);
		}

		return films;
	}

	/**
	 * Fetches a film from the database based on the provided film ID.
	 * @param id The ID of the film to be fetched.
	 * @return Film object containing the details of the film with the provided ID.
	 */
	public Film getFilmByID(int id) {
		String sql = "SELECT * FROM films WHERE id = ?";
		Film film = null;
		try (Connection conn = getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setInt(1, id);
			try (ResultSet rs = pstmt.executeQuery()) {
				if (rs.next()) {
					film = getNextFilm(rs);
				}
			}
		} catch (SQLException e) {
			System.err.println("SQL Error: " + e.getMessage());
		}
		return film;
	}

	/**
	 * Searches for films in the database based on the provided search query an search type. 
	 * @param searchQuery The search query to be used for searching films.
	 * @param searchType  The type of search to be performed (all, id, title, director, year, stars).
	 * @return List of Film objects containing the films that match the search query.
	 */
	public List<Film> searchFilms(String searchQuery, String searchType) {
		List<Film> films = new ArrayList<>();
		StringBuilder sql = new StringBuilder("SELECT * FROM films");

		switch (searchType) {
		case "all":
			sql.append(" WHERE id LIKE ? OR title LIKE ? OR director LIKE ? OR stars LIKE ? OR year LIKE ?");
			break;
		case "id":
			sql.append(" WHERE id = ?");
			break;
		case "title":
			sql.append(" WHERE title LIKE ?");
			break;
		case "director":
			sql.append(" WHERE director LIKE ?");
			break;
		case "year":
			sql.append(" WHERE year = ?");
			break;
		case "stars":
			sql.append(" WHERE stars LIKE ?");
			break;
		default:
			return getAllFilms();
		}

		try (Connection conn = getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql.toString())) {

			if (searchType.equals("all")) {
				pstmt.setString(1, "%" + searchQuery + "%");
				pstmt.setString(2, "%" + searchQuery + "%");
				pstmt.setString(3, "%" + searchQuery + "%");
				pstmt.setString(4, "%" + searchQuery + "%");
				pstmt.setString(5, "%" + searchQuery + "%");
			} else {
				if (searchType.equals("year") || searchType.equals("id")) {
					pstmt.setInt(1, Integer.parseInt(searchQuery));
				} else {
					pstmt.setString(1, "%" + searchQuery + "%");
				}
			}

			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				Film film = getNextFilm(rs);
				JSONObject movieData = tmdbApiDAO.fetchMovieData(film.getTitle(), film.getYear());
				if (movieData != null && movieData.has("poster_path")) {
					String posterUrl = "https://image.tmdb.org/t/p/w500" + movieData.getString("poster_path");
					film.setPosterUrl(posterUrl);
				} else {
					film.setPosterUrl("https://via.placeholder.com/500");
				}
				films.add(film);
			}
		} catch (SQLException e) {
			System.err.println("SQL Error: " + e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return films;
	}

	/**
	 * Searches for films in the database based on the provided search query.
	 * @param searchQuery The search query to be used for searching films.
	 * @return List of Film objects containing the films that match the search query.
	 */	
	public List<Film> searchFilms(String searchQuery) {
		String sql = "SELECT * FROM films WHERE id LIKE ? OR title LIKE ? OR director LIKE ? OR stars LIKE ? OR year LIKE ?";
		List<Film> films = new ArrayList<>();
		TMDbApiDAO tmdbApiDAO = new TMDbApiDAO();

		try (Connection conn = getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
			for (int i = 1; i <= 5; i++) {
				pstmt.setString(i, "%" + searchQuery + "%");
			}
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				Film film = getNextFilm(rs);

				try {
					JSONObject movieData = tmdbApiDAO.fetchMovieData(film.getTitle(), film.getYear());
					if (movieData != null) {
						// Poster
						String posterPath = movieData.optString("poster_path");
						film.setPoster(posterPath != null ? "https://image.tmdb.org/t/p/w500" + posterPath
								: "https://via.placeholder.com/500");

						// Trailer Key
						String movieId = tmdbApiDAO.getMovieId(movieData);
						if (movieId != null) {
							JSONObject movieDetails = tmdbApiDAO.fetchMovieDetails(movieId);
							String trailerKey = extractTrailerKey(movieDetails.optJSONObject("videos"));
							film.setTrailerKey(trailerKey);
						}
					}
				} catch (Exception e) {
					System.err.println("Error fetching movie data: " + e.getMessage());
					film.setPoster("https://via.placeholder.com/500");
				}

				films.add(film);
			}
		} catch (SQLException e) {
			System.err.println("SQL Error: " + e.getMessage());
		}
		return films;
	}

	/**
	 * Extracts the trailer key from the provided videos JSON object.
	 * @param videos JSON object containing movie videos data.
	 * @return String containing the trailer key if found, null otherwise.
	 */
	private String extractTrailerKey(JSONObject videos) {
		if (videos != null && videos.has("results")) {
			JSONArray results = videos.getJSONArray("results");
			for (int i = 0; i < results.length(); i++) {
				JSONObject video = results.getJSONObject(i);
				if ("Trailer".equals(video.optString("type")) && "YouTube".equals(video.optString("site"))) {
					return video.optString("key");
				}
			}
		}
		return null;
	}

	/**
	 * Inserts a new film into the database.
	 * @param film Film object containing the details of the film to be inserted.
	 */
	public void insertFilm(Film film) {
		String sql = "INSERT INTO films (title, year, director, stars, review) VALUES (?, ?, ?, ?, ?)";
		try (Connection conn = getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setString(1, film.getTitle());
			pstmt.setInt(2, film.getYear());
			pstmt.setString(3, film.getDirector());
			pstmt.setString(4, film.getStars());
			pstmt.setString(5, film.getReview());
			pstmt.executeUpdate();
		} catch (SQLException e) {
			System.err.println("Insert Error: " + e.getMessage());
		}
	}

	/**
	 * Updates an existing film in the database.
	 * @param film Film object containing the updated details of the film.
	 */
	public void updateFilm(Film film) {
		String sql = "UPDATE films SET title = ?, year = ?, director = ?, stars = ?, review = ? WHERE id = ?";
		try (Connection conn = getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setString(1, film.getTitle());
			pstmt.setInt(2, film.getYear());
			pstmt.setString(3, film.getDirector());
			pstmt.setString(4, film.getStars());
			pstmt.setString(5, film.getReview());
			pstmt.setInt(6, film.getId());
			pstmt.executeUpdate();
		} catch (SQLException e) {
			System.err.println("Update Error: " + e.getMessage());
		}
	}

	/**
	 * Deletes a film from the database based on the provided film ID.
	 * @param id The ID of the film to be deleted.
	 */
	public void deleteFilm(int id) {
		String sql = "DELETE FROM films WHERE id = ?";
		try (Connection conn = getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setInt(1, id);
			pstmt.executeUpdate();
		} catch (SQLException e) {
			System.err.println("Delete Error: " + e.getMessage());
			e.printStackTrace();
		}
	}

	/**
	 * Fetches the total number of films stored in the database.
	 * @return Integer containing the total number of films in the database.
	 * @throws SQLException if a database access error occurs.
	 */
	public int getTotalFilmCount() throws SQLException {
		String sql = "SELECT COUNT(*) FROM films";
		try (Connection conn = getConnection();
				PreparedStatement pstmt = conn.prepareStatement(sql);
				ResultSet rs = pstmt.executeQuery()) {
			if (rs.next()) {
				return rs.getInt(1);
			}
		} catch (SQLException e) {
			System.err.println("Error fetching total film count: " + e.getMessage());
			throw e;
		}
		return 0;
	}

	/**
	 * Fetches a paginated list of films from the database.
	 * @param page     The page number of the films to be fetched.
	 * @param pageSize The number of films to be fetched per page.
	 * @return List of Film objects containing the films for the specified page.
	 * @throws Exception if a database access error occurs.
	 */	
	public List<Film> getFilmsPaginated(int page, int pageSize) throws Exception {
		List<Film> films = new ArrayList<>();
		String sql = "SELECT * FROM films LIMIT ?, ?";
		int start = (page - 1) * pageSize;
		try (Connection conn = getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setInt(1, start);
			pstmt.setInt(2, pageSize);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				Film film = getNextFilm(rs);
				JSONObject movieData = tmdbApiDAO.fetchMovieData(film.getTitle(), film.getYear());
				if (movieData != null && movieData.has("poster_path")) {
					String posterUrl = "https://image.tmdb.org/t/p/w500" + movieData.getString("poster_path");
					film.setPosterUrl(posterUrl);
				} else {
					film.setPosterUrl("https://via.placeholder.com/500");
				}
				films.add(film);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return films;
	}

	/**
	 * Fetches a random list of films from the database.
	 * @param limit The maximum number of films to be fetched.
	 * @return List of Film objects containing the randomly fetched films.
	 */
	public List<Film> getRandomFilms(int limit) {
		List<Film> films = new ArrayList<>();
		try (Connection conn = getConnection();
				PreparedStatement ps = conn.prepareStatement("SELECT * FROM films ORDER BY RAND() LIMIT ?")) {
			ps.setInt(1, limit);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				films.add(getNextFilm(rs));
			}
		} catch (SQLException e) {
			System.err.println("Error retrieving random films: " + e.getMessage());
		}
		return films;
	}

}