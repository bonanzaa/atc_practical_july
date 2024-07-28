package com.example.demo.service;

import com.example.demo.exception.DuplicateFoundException;
import com.example.demo.exception.MovieNotFoundException;
import com.example.demo.model.Movie;
import com.example.demo.model.Review;
import com.example.demo.repository.MovieRepository;
import com.example.demo.repository.ReviewRepository;
import org.apache.commons.collections4.IteratorUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class MovieService {

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private ReviewRepository reviewRepository;

    public List<Movie> getAllMovies(){
        return movieRepository.findAll();
    }

    public Movie addMovie(Movie movie){
        return movieRepository.save(movie);
    }
    public List<Movie> addMovies(List<Movie> movies){
        List<Movie> result = new ArrayList<>();
        for(Movie movie : movies){
            Boolean duplicate = isDuplicate(movie);
            if(Boolean.FALSE.equals(duplicate)) result.add(addMovie(movie));
        }
        return result;
    }
    public Movie getMovieById(Long id) throws MovieNotFoundException {
        Optional<Movie> result = movieRepository.findById(id);
        if(result.isEmpty()) throw new MovieNotFoundException("No movie with such ID was found.");
        return result.get();
    }
    public float calculateAverageRating(Movie movie){
        List<Review> reviews = reviewRepository.findByMovie(movie);
        if(reviews.isEmpty()) return 0f;

        int sum = reviews.stream().mapToInt(Review::getRating).sum();
        float result = (float) sum / reviews.size();
        DecimalFormat df = new DecimalFormat("#.#");
        float roundedRating = Float.parseFloat(df.format(result));
        movie.setRating(roundedRating);
        movie.setReviewCount(reviews.size());

        movieRepository.save(movie);

        return movie.getRating();
    }

    private Boolean isDuplicate(Movie movie){
        return movieRepository.getMovieByTitleAndDirector(movie.getTitle(), movie.getDirector()).isPresent();
    }
}
