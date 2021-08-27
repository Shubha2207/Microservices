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
    public List<CatalogItem> getCatalogItems(@PathVariable("userId") String userId) {



        List<Rating> ratings = Arrays.asList(
                new Rating("AvengerIW", 4),
                new Rating("AvengerEG", 5)
        );


        // iterating through list of ratings
        return ratings.stream().map(rating -> {
            // getForObject method unmarshell response from string to java object
            // must include empty constructor in the movie class
            // otherwise it wont be able to create an object of that class and copy data into it
            Movie movie = restTemplate.getForObject("http://localhost:8082/movies/"+rating.getMovieId(),Movie.class);
            return new CatalogItem(rating.getMovieId(), movie.getDescription(), rating.getRating());
        }).collect(Collectors.toList());

    }

}
