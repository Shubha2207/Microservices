package com.example.ratingservice.resources;

import com.example.ratingservice.models.Rating;
import com.example.ratingservice.models.UserRating;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/ratings")
public class RatingResource {

    @RequestMapping("/{movieId}")
    public Rating getRating(@PathVariable("movieId") String movieId){
        return new Rating(movieId, 4);
    }


    @RequestMapping("/users/{userId}")
    public UserRating getUserRating(@PathVariable("userId") String userId){
        List<Rating> ratings = Arrays.asList(
                new Rating("AvengerIW", 4),
                new Rating("AvengerEG", 5)
        );

        UserRating userRating = new UserRating();
        userRating.setRatingList(ratings);

        return userRating;
    }
}
