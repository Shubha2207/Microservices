package com.java.microservice.moviecatalogservice.controller;

import com.java.microservice.moviecatalogservice.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/catalog")
public class MovieCatalogResource {

    @Autowired
    private RestTemplate restTemplate; // taking value from restTemplate bean

    @Autowired
    private WebClient.Builder webClient;

    @Value("${rating-data-service.base.url}")
    private String RATING_DATA_SERVICE_URL;

    @Value("${movie-info-service.base.url}")
    private String MOVIE_INFO_SERVICE_URL;

    @GetMapping("/{userID}")
    public UserCatalogItem getCatalogItems(@PathVariable("userID") String userID){

//        RestTemplate restTemplate = new RestTemplate();



        // get all movieIDs and ratings from rating-data-service
//        List<Rating> ratings = Arrays.asList(
//                new Rating("A1020",3.5),
//                new Rating("B2233",4)
//        );

        /*
        // hardcoding url is a bad practice
        UserRating userRating = restTemplate.getForObject(
                "http://localhost:8083/ratings/users/"+userID,
                UserRating.class);
         */

        /*
        // taking value from property file
        UserRating userRating = restTemplate.getForObject(
                RATING_DATA_SERVICE_URL+userID,
                UserRating.class);
         */

        // Service-discovery enabled so no need to fetch value from property file
        UserRating userRating = restTemplate.getForObject(
                "http://rating-data-service/ratings/users/"+userID,
                UserRating.class);


        // get info of each movie from list of movieids
//        List<Movie> movies = Arrays.asList();
//        ratings.stream().map(
//                rating -> movies.add(new Movie(rating.getMovieID(),"Transformer","Scifi Movie"))
//
//        );



        // consolidate the result

        /*
        // return hardcoded list
        return Collections.singletonList(
                new CatalogItem("Transformer","SciFi movie",3.5)
        );

         */


        /*
        // taking values from hardcoded ratings list
        return ratings.stream().map(rating ->
        {
            Movie movie = restTemplate.getForObject("http://localhost:8082/movies/"+rating.getMovieID(),Movie.class);
            return new CatalogItem(movie.getName(),movie.getDescription(),rating.getRating());
        }).collect(Collectors.toList());

         */

        /*
        // Synchronous webclient way of calling rest service using block() method
        return ratings.stream().map(rating -> {
            Movie movie = webClient.build()
                    .get()
                    .uri("http://localhost:8082/movies/"+rating.getMovieID())
                    .retrieve()
                    .bodyToMono(Movie.class)
                    .block();

            return new CatalogItem(movie.getName(),movie.getDescription(),rating.getRating());
        }).collect(Collectors.toList());
        */

        // taking data from userRating object
        List<Rating> ratings = userRating.getUserRatings();

        // Not a good practice to return a list
//        return ratings.stream().map(rating ->
//        {
//            // get info of each movie from list of movieids
//            Movie movie = restTemplate.getForObject("http://localhost:8082/movies/"+rating.getMovieID(),Movie.class);
//            // consolidate the result
//            return new CatalogItem(movie.getName(),movie.getDescription(),rating.getRating());
//        }).collect(Collectors.toList());

        // return UserCatalogItem object
        List<CatalogItem> userCatalogItem = new ArrayList<>();

        for (Rating rating:ratings) {
//            Movie movie = restTemplate.getForObject(MOVIE_INFO_SERVICE_URL+rating.getMovieID(),Movie.class);

            // since eureka service discovery is enabled, we can directly call the service using just the name of that service
            Movie movie = restTemplate.getForObject("http://movie-info-service/movies/"+rating.getMovieID(),Movie.class);
            CatalogItem catalogItem = new CatalogItem(movie.getName(), movie.getDescription(), rating.getRating());
            userCatalogItem.add(catalogItem);
        }

        return new UserCatalogItem(userID,userCatalogItem);

    }
}
