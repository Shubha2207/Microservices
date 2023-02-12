package com.java.microservice.movieinfoservice.controller;


import com.java.microservice.movieinfoservice.model.Movie;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/movies")
public class MovieInfo {

    @GetMapping("/{movieID}")
    public Movie getMovieInfo(@PathVariable("movieID") String movieID){
        return new Movie(movieID,"Trasformer","Scifi movie");
    }
}
