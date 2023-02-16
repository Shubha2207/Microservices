package com.java.microservice.movieinfoservice.controller;


import com.java.microservice.movieinfoservice.model.Movie;
import com.java.microservice.movieinfoservice.model.MovieSummary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/movies")
public class MovieInfo {

    @Autowired
    private RestTemplate restTemplate;

    @Value("${api_key}")
    private String API_KEY;


    @GetMapping("/{movieID}")
    public Movie getMovieInfo(@PathVariable("movieID") String movieID){
//        return new Movie(movieID,"Trasformer","Scifi movie");
        MovieSummary movieSummary = restTemplate.getForObject("https://api.themoviedb.org/3/movie/"+movieID+"?api_key="+API_KEY, MovieSummary.class);
        return new Movie(movieID,movieSummary.getOriginal_title(),movieSummary.getOverview());
    }
}
