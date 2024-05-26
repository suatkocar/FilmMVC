package model;

/**
 * Represents the model class for a Film object within the application.
 * Holds details about a film and provides methods to retrieve or update these details.
 */

public class Film {
    // Class fields for storing film properties
    private int id;
    private String title;
    private int year;
    private String director;
    private String stars;
    private String review;
    private String poster;
    private String backdrop;
    private String trailerKey;
    private String posterUrl;

    /**
     * Default constructor for creating an empty Film object.
     */
    public Film() {
    }

    /**
     * Constructor for creating a Film object with all properties except poster and backdrop URLs.
     * @param id The film's unique identifier
     * @param title The title of the film
     * @param year The release year of the film
     * @param director The director of the film
     * @param stars The main stars of the film
     * @param review A review or description of the film
     */
    public Film(int id, String title, int year, String director, String stars, String review) {
        this.id = id;
        this.title = title;
        this.year = year;
        this.director = director;
        this.stars = stars;
        this.review = review;
    }

    // Getter and setter methods for all properties

    public String getPosterUrl() {
        return posterUrl;
    }

    public void setPosterUrl(String posterUrl) {
        this.posterUrl = posterUrl;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String getDirector() {
        return director;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public String getStars() {
        return stars;
    }

    public void setStars(String stars) {
        this.stars = stars;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }

    public String getPoster() {
        return poster;
    }

    /**
     * Sets the URL of the film's poster. Prefixes the provided path with a base URL if the path is not null or empty.
     * @param poster Path fragment of the poster image.
     */
    public void setPoster(String poster) {
        this.poster = (poster != null && !poster.isEmpty()) ? "https://image.tmdb.org/t/p/w500" + poster : null;
    }

    public String getBackdrop() {
        return backdrop;
    }

    /**
     * Sets the URL of the film's backdrop. Prefixes the provided path with a full URL, ensuring it's a valid link.
     * @param backdrop Path fragment of the backdrop image.
     */
    public void setBackdrop(String backdrop) {
        this.backdrop = (backdrop != null && !backdrop.isEmpty()) ? "https://image.tmdb.org/t/p/original" + backdrop
                : null;
    }

    public String getTrailerKey() {
        return trailerKey;
    }

    public void setTrailerKey(String trailerKey) {
        this.trailerKey = trailerKey;
    }

    /**
     * Returns a string representation of the film object.
     * Includes details with id, title, year, director, stars, and review.
     */
    @Override
    public String toString() {
        return "Film [id=" + id + ", title=" + title + ", year=" + year + ", director=" + director + ", stars=" + stars
                + ", review=" + review + "]";
    }
}
