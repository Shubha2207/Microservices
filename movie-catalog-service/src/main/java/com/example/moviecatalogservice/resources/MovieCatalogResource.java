package com.example.moviecatalogservice.resources;

import com.example.moviecatalogservice.models.CatalogItem;
import com.example.moviecatalogservice.models.Movie;
import com.example.moviecatalogservice.models.Rating;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

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

    @Autowired
    private WebClient.Builder webClientBuilder;


    @RequestMapping("/{userId}")
    public List<CatalogItem> getCatalogItems(@PathVariable("userId") String userId) {

//        WebClient.Builder builder = WebClient.builder();

        List<Rating> ratings = Arrays.asList(
                new Rating("AvengerIW", 4),
                new Rating("AvengerEG", 5)
        );


        // iterating through list of ratings
        return ratings.stream().map(rating -> {

//            Movie movie = restTemplate.getForObject("http://localhost:8082/movies/"+rating.getMovieId(),Movie.class);

            Movie movie = webClientBuilder.build()
                    .get()
                    .uri("http://localhost:8082/movies/"+rating.getMovieId())
                    .retrieve()
                    .bodyToMono(Movie.class)
                    .block() ;

            // build creates the object | get() tells this get method | uri
            // retrive() gets the response
            // bodyToMono tells to wait until reponse is ready
            // block() makes the execution block until mono is filled with the movie object

            return new CatalogItem(rating.getMovieId(), movie.getDescription(), rating.getRating());
        }).collect(Collectors.toList());

    }

}
