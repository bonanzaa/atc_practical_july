package com.example.demo.service;

import com.example.demo.model.Movie;
import com.example.demo.model.Review;
import com.example.demo.model.ReviewDTO;
import com.example.demo.repository.MovieRepository;
import com.example.demo.repository.ReviewRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ReviewServiceTest {
    @Mock
    private ReviewRepository reviewRepository;

    @Mock
    private MovieRepository movieRepository;

    @Spy
    @InjectMocks
    private ReviewService reviewService;

    @Mock
    private MovieService movieService;

    @Test
    void addNewReviewSuccess() throws Exception {
        Review newReview = new Review().toBuilder()
                .username("Hello")
                .rating(10)
                .build();
        Movie newMovie = new Movie().toBuilder()
                .title("Test")
                .director("Test").build();


        when(movieService.getMovieById(any(Long.class))).thenReturn(newMovie);
        when(reviewRepository.save(any(Review.class))).thenReturn(newReview);
        Review result = reviewService.addNewReview(newReview,1L);

        assertEquals(10,result.getRating());
        assertEquals(newMovie,result.getMovie());
    }

    @Test
    void addNewReviewInvalidRangeFailure() throws Exception {
        Throwable exception = assertThrows(Exception.class,() ->
                reviewService.addNewReview(new Review().toBuilder().build(),0L));

        assertEquals("Invalid rating submitted. Range : 1-10",exception.getMessage());
    }

    @Test
    void editReviewSuccess() throws Exception {
        Review oldReview = new Review().toBuilder()
                .reviewText("Test 1")
                .rating(5)
                .build();
        Review newReview = new Review().toBuilder()
                .reviewText("Test 2")
                .rating(10)
                .build();

        when(reviewRepository.findById(any(Long.class))).thenReturn(Optional.of(oldReview));
        when(reviewRepository.save(any(Review.class))).thenReturn(newReview);

        Review result = reviewService.editExistingReview(newReview,1L);

        assertEquals(10,result.getRating());
    }

    @Test
    void editReviewFailure() throws Exception {
        when(reviewRepository.findById(any(Long.class))).thenReturn(Optional.empty());

        Throwable exception = assertThrows(Exception.class,() ->
                reviewService.editExistingReview(new Review().toBuilder().build(),0L));

        assertEquals("No review with such ID was found.",exception.getMessage());
    }

    @Test
    void getReviewsForMovieSuccess(){
        List<Review> reviews = List.of(
          new Review().toBuilder().build(),
          new Review().toBuilder().build()
        );

        when(reviewRepository.findByMovieId(any(Long.class))).thenReturn(reviews);

        List<ReviewDTO> result = reviewService.getReviewsForMovieId(1L);

        assertEquals(2,result.size());
    }
}
