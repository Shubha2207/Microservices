package com.example.movieinfoservice.resources;

import com.example.movieinfoservice.models.Movie;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/movies")
public class MovieResource {


    @RequestMapping("/{movieId}")
    public Movie getMovies(@PathVariable("movieId") String movieId){

        List<Movie> movies = Arrays.asList(
                new Movie("AvengerEG", "Avenger End Game"),
                new Movie("AvengerIW","Avenger Infinity War")
        );

        Movie result = new Movie();

        movies.forEach(movie -> {
            if(movie.getMovieId().equals(movieId)){
                result.setMovieId(movie.getMovieId());
                result.setDescription(movie.getDescription());
            }

        });
        return result;
    }
}
