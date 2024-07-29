package com.example.demo.controller;

import com.example.demo.model.Movie;
import com.example.demo.service.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("movies")
public class MovieController {

    @Autowired
    private MovieService movieService;

    @GetMapping("/all")
    public ResponseEntity<List<Movie>> getAllMovies(){
        return new ResponseEntity<>(movieService.getAllMovies(),HttpStatus.OK);
    }
    @PostMapping("/new")
    public ResponseEntity<List<Movie>> addMovies(@RequestBody List<Movie> movies) throws Exception {
        List<Movie> result = movieService.addMovies(movies);
        return new ResponseEntity<>(result,HttpStatus.OK);
    }


}
