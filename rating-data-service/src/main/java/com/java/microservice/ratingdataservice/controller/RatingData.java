package com.java.microservice.ratingdataservice.controller;


import com.java.microservice.ratingdataservice.model.Rating;
import com.java.microservice.ratingdataservice.model.UserRating;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/ratings")
public class RatingData {

    @GetMapping("/{movieID}")
    public Rating getRatingData(@PathVariable("movieID") String movieID){
        return new Rating(movieID,4);
    }

    @GetMapping("/users/{userID}")
    public UserRating getUserRatings(@PathVariable("userID") String userID){
        List<Rating> ratings = Arrays.asList(
                new Rating("A1020",3.5),
                new Rating("B2233",4)
        );
        // return ratings;
        // return list is a bad practice becuase if in future you need add new filds to object, you cannot do that
        // hence always return object which can contain list
        return new UserRating(userID,ratings);
    }
}
