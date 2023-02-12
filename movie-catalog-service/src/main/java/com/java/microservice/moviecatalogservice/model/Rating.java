package com.java.microservice.moviecatalogservice.model;

public class Rating {
    private String movieID;
    private double rating;

    public Rating() {
    }

    public Rating(String movieID, double rating) {
        this.movieID = movieID;
        this.rating = rating;
    }

    public String getMovieID() {
        return movieID;
    }

    public void setMovieID(String movieID) {
        this.movieID = movieID;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }
}
