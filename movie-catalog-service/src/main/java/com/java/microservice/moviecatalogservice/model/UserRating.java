package com.java.microservice.moviecatalogservice.model;

import java.util.List;

public class UserRating {
    private String userID;
    private List<Rating> userRatings;

    public UserRating() {
    }

    public UserRating(String userID, List<Rating> userRatings) {
        this.userID = userID;
        this.userRatings = userRatings;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public List<Rating> getUserRatings() {
        return userRatings;
    }

    public void setUserRatings(List<Rating> userRatings) {
        this.userRatings = userRatings;
    }
}

