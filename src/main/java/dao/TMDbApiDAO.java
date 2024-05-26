package dao;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import model.Film;

/**
 * Data Access Object class to interact with The Movie Database (TMDb) API.
 */
public class TMDbApiDAO {
	private String apiKey;

	/**
	 * Constructor to initialise the API key for TMDb.
	 */
	
	// MY API key for TMDb - This is normally stored in a secure location like a properties file or environment variable.
	// But for the purpose of this project and for the Assignment it was stored here.
	public TMDbApiDAO() {
		this.apiKey = "47f68eb8da5868bbf454860765e08368"; 
	}

	/**
	 * Method to make an API request to TMDb and return the response as a JSONObject.
	 * @param endpoint The URL endpoint to make the API request to
	 * @return The response from the API as a JSONObject
	 * @throws Exception if there is an issue with the API request
	 */
	private JSONObject makeApiRequest(String endpoint) throws Exception {
		@SuppressWarnings("deprecation")
		URL url = new URL(endpoint);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setRequestMethod("GET");
		conn.setRequestProperty("Content-Type", "application/json");

		try (BufferedReader br = new BufferedReader(
				new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8))) {
			StringBuilder response = new StringBuilder();
			String line;
			while ((line = br.readLine()) != null) {
				response.append(line);
			}
			return new JSONObject(response.toString());
		}
	}

	/**
	 * Method to fetch movie data from TMDb based on the title and year of the movie.
	 * @param title The title of the movie
	 * @param year  The year of the movie
	 * @return The movie data as a JSONObject
	 * @throws Exception if there is an issue with the API request
	 */
	public JSONObject fetchMovieData(String title, int year) throws Exception {
		title = URLEncoder.encode(title, StandardCharsets.UTF_8.toString());
		String endpoint = "https://api.themoviedb.org/3/search/movie?api_key=" + apiKey + "&query=" + title;
		JSONObject response = makeApiRequest(endpoint);
		JSONArray results = response.optJSONArray("results");

		if (results != null) {
			for (int i = 0; i < results.length(); i++) {
				JSONObject movieData = results.getJSONObject(i);
				String releaseDate = movieData.optString("release_date", "");
				int movieYear = -1;

				if (!releaseDate.isEmpty() && releaseDate.contains("-")) {
					movieYear = Integer.parseInt(releaseDate.substring(0, releaseDate.indexOf('-')));
				}

				if ((movieYear == year || movieYear == year - 1 || movieYear == year - 2 || movieYear == year + 1
						|| movieYear == year + 2) && !movieData.optString("poster_path", "").isEmpty()) {
					return movieData;
				}
			}
		}
		System.out.println("No matching results found for " + title + " with close year match");
		return null;
	}

	/**
	 * Method to extract the movie ID from the movie data.
	 * @param movieData The movie data as a JSONObject
	 * @return The movie ID as a String
	 */
	public String getMovieId(JSONObject movieData) {
		if (movieData != null && movieData.has("id")) {
			return movieData.get("id").toString();
		}
		return null;
	}

	/**
	 * Method to fetch detailed movie data from TMDb based on the movie ID.
	 * @param movieId The ID of the movie
	 * @return The detailed movie data as a JSONObject
	 * @throws Exception if there is an issue with the API request
	 */
	public JSONObject fetchMovieDetails(String movieId) throws Exception {
		String endpoint = "https://api.themoviedb.org/3/movie/" + movieId + "?api_key=" + apiKey
				+ "&append_to_response=videos";
		return makeApiRequest(endpoint);
	}

	public String extractTrailerKey(JSONObject videos) {
		for (int i = 0; i < videos.getJSONArray("results").length(); i++) {
			JSONObject video = videos.getJSONArray("results").getJSONObject(i);
			if ("Trailer".equals(video.optString("type")) && "YouTube".equals(video.optString("site"))) {
				return video.optString("key");
			}
		}
		return null;
	}

	/**
	 * Method to process a list of films and return a list of films that meet the criteria for the hero slide.
	 * @param filmsToProcess The list of films to process
	 * @return The list of processed films
	 */
	public List<Film> getProcessedFilms(List<Film> filmsToProcess) {
		List<Film> processedFilms = new ArrayList<>();
		for (Film film : filmsToProcess) {
			try {
				JSONObject movieData = fetchMovieData(film.getTitle(), film.getYear());
				if (movieData == null)
					continue;

				String movieId = getMovieId(movieData);
				if (movieId == null)
					continue;

				JSONObject movieDetails = fetchMovieDetails(movieId);
				if (movieDetails == null)
					continue;

				if (processedFilms.size() >= 10)
					break;

				if (movieDetails.has("backdrop_path") && movieDetails.getJSONObject("videos").has("results")) {
					JSONObject videos = movieDetails.getJSONObject("videos");
					String trailerKey = extractTrailerKey(videos);
					if (trailerKey != null) {
						film.setBackdrop(
								"https://image.tmdb.org/t/p/original" + movieDetails.optString("backdrop_path"));
						film.setPoster("https://image.tmdb.org/t/p/w500" + movieDetails.optString("poster_path"));
						film.setTrailerKey(trailerKey);
						processedFilms.add(film);
					}
				}

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return processedFilms;
	}
}