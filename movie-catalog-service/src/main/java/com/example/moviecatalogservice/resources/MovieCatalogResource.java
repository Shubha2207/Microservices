package com.example.moviecatalogservice.resources;

import com.example.moviecatalogservice.models.*;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;


import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/catalog")
public class MovieCatalogResource {

    @Autowired
    private RestTemplate restTemplate;
    // only single object is created and used
    // function with @Bean annotation provides the object to this reference


    @RequestMapping("/{userId}")
    public Catalog getCatalogItems(@PathVariable("userId") String userId) {


//        List<Rating> ratings = Arrays.asList(
//                new Rating("AvengerIW", 4),
//                new Rating("AvengerEG", 5)
//        );

        UserRating userRating = restTemplate.getForObject("http://eureka-client-movie-rating-service/ratings/users/"+userId, UserRating.class);

        // iterating through list of ratings
//        return userRating.getRatingList().stream().map(rating -> {
//
//            Movie movie = restTemplate.getForObject("http://localhost:8082/movies/"+rating.getMovieId(),Movie.class);
//
//
//
//            return new CatalogItem(rating.getMovieId(), movie.getDescription(), rating.getRating());
//        }).collect(Collectors.toList());

        Catalog catalog = new Catalog();

        userRating.getRatingList().forEach(rating -> {
            Movie movie = restTemplate.getForObject("http://eureka-client-movie-info-service/movies/"+rating.getMovieId(),Movie.class);

            catalog.setSingleCatalogItem(new CatalogItem(rating.getMovieId(),movie.getDescription(),rating.getRating()));

        });

        return catalog;

    }

}
