package com.example.movieinfoservice.models;

public class Movie {

    private String movieId;
    private String description;

    public Movie(String movieId, String description) {
        this.movieId = movieId;
        this.description = description;
    }

    public String getMovieId() {
        return movieId;
    }

    public void setMovieId(String movieId) {
        this.movieId = movieId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
