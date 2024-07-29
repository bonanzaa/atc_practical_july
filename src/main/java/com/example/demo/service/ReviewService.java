package com.example.demo.service;

import com.example.demo.exception.InvalidArgumentsException;
import com.example.demo.exception.ReviewNotFoundException;
import com.example.demo.model.Movie;
import com.example.demo.model.Review;
import com.example.demo.model.ReviewDTO;
import com.example.demo.repository.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ReviewService {
    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private MovieService movieService;

    private static final String USERNAME_TOO_LONG = "Provided username is too long - max length is: 15.";
    private static final String USERNAME_TOO_SHORT = "Provided username is too short - min length is: 3.";
    private static final String USERNAME_INVALID = "Provided username cannot contain any special characters.";
    private static final String USERNAME_VALIDATION = "^[a-zA-Z0-9]*$";

    public Review addNewReview(Review review,Long movieId) throws Exception {
        validateRating(review);
        validateUsername(review.getUsername());

        Review existing = reviewExists(review.getUsername(),movieId);
        if(existing != null){
            return editExistingReview(review,existing.getId());
        }
        Movie movie = movieService.getMovieById(movieId);
        review.setMovie(movie);
        String date = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm"));
        review.setDate(date);

        String username = review.getUsername();
        review.setUsername(username.toLowerCase());

        Review result = reviewRepository.save(review);
        movieService.calculateAverageRating(movie);
        return result;
    }

    public Review editExistingReview(Review review,Long reviewId) throws ReviewNotFoundException{
        Optional<Review> existingReview = reviewRepository.findById(reviewId);
        if(existingReview.isEmpty()) throw new ReviewNotFoundException("No review with such ID was found.");

        Review existing = existingReview.get();
        existing.setReviewText(review.getReviewText());
        existing.setRating(review.getRating());

        movieService.calculateAverageRating(existing.getMovie());
        return reviewRepository.save(existing);
    }

    public List<ReviewDTO> getReviewsForMovieId(Long id){
        List<Review> reviewList = reviewRepository.findByMovieId(id);
        List<ReviewDTO> reviewDTOList = new ArrayList<>();
        for(Review review : reviewList){
            reviewDTOList.add(
                    new ReviewDTO().toBuilder()
                            .username(review.getUsername())
                            .rating(review.getRating())
                            .reviewText(review.getReviewText())
                            .build()
            );
        }
        return reviewDTOList;
    }

    public List<Review> getReviewsByUsername(String username){
        return reviewRepository.findByUsername(username);
    }

    private void validateRating(Review review) throws InvalidArgumentsException {
        float rating = review.getRating();

        if(rating < 1 || rating > 10) throw new InvalidArgumentsException("Invalid rating submitted. Range : 1-10");
    }

    private void validateUsername(String username) throws InvalidArgumentsException {
        String message = "";
        if(username.length() > 15) message=USERNAME_TOO_LONG;
        if(username.length() <3) message=USERNAME_TOO_SHORT;
        if(!username.matches(USERNAME_VALIDATION)) message=USERNAME_INVALID;
        if(!message.isEmpty()) throw new InvalidArgumentsException(message);
    }

    public Review reviewExists(String username,Long movieId) {
        return reviewRepository.findExistingReviews(username.toLowerCase(),movieId);
    }
}
