package com.example.demo.service;

import com.example.demo.model.Movie;
import com.example.demo.model.Review;
import com.example.demo.repository.MovieRepository;
import com.example.demo.repository.ReviewRepository;
import com.example.demo.service.MovieService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MovieServiceTest {

    @Mock
    private MovieRepository movieRepository;

    @Mock
    private ReviewRepository reviewRepository;

    @Spy
    @InjectMocks
    private MovieService service;

    @Test
    void addMoviesSuccess(){
        List<Movie> movies = List.of(
                new Movie().toBuilder()
                        .title("asd")
                        .director("asd")
                        .build(),
                new Movie().toBuilder()
                        .title("asd")
                        .director("asd")
                        .build()
        );

        when(movieRepository.getMovieByTitleAndDirector(any(String.class),any(String.class))).thenReturn(Optional.empty());
        when(movieRepository.save(any(Movie.class))).thenReturn(new Movie().toBuilder().title("asd").director("asd").build());
        List<Movie> result = service.addMovies(movies);

        assertEquals(movies,result);
    }

    @Test
    void getAllMoviesTest(){
        List<Movie> movies = List.of(
                new Movie().toBuilder()
                        .title("asd")
                        .director("asd")
                        .build(),
                new Movie().toBuilder()
                        .title("asd")
                        .director("asd")
                        .build()
        );
        when(movieRepository.findAll()).thenReturn(movies);
        List<Movie> result = service.getAllMovies();
        verify(movieRepository,times(1)).findAll();
        assertEquals(movies,result);
    }

    @Test
    void getMovieByIdSuccess() throws Exception {
        Movie movie = new Movie().toBuilder().build();

        when(movieRepository.findById(any(Long.class))).thenReturn(Optional.of(movie));

        Movie result = service.getMovieById(1L);

        assertEquals(movie,result);
    }

    @Test
    void getMovieByIdFail() throws Exception {
        when(movieRepository.findById(any(Long.class))).thenReturn(Optional.empty());

        Throwable exception = assertThrows(Exception.class,() ->
                service.getMovieById(1L));

        assertEquals("No movie with such ID was found.",exception.getMessage());
    }

    @Test
    void addMovieSuccess() throws Exception {
        Movie newMovie = new Movie().toBuilder()
                .title("Test")
                .director("Test")
                .build();
        when(movieRepository.save(any(Movie.class))).thenReturn(newMovie);
        Movie result = service.addMovie(newMovie);

        assertEquals(result,newMovie);
    }

    @Test
    void calculateAverageRatingSuccess() throws Exception {
        Movie newMovie = new Movie().toBuilder()
                .title("Test")
                .director("Test")
                .build();

        List<Review> reviews = List.of(
                new Review().toBuilder()
                        .rating(10).build(),
                new Review().toBuilder()
                        .rating(1).build()
        );

        when(reviewRepository.findByMovie(any(Movie.class))).thenReturn(reviews);
        when(movieRepository.save(any(Movie.class))).thenReturn(newMovie);
        float result = service.calculateAverageRating(newMovie);

        assertEquals(5.5,result);
    }

    @Test
    void calculateAverageRatingFailure() throws Exception {
        Movie newMovie = new Movie().toBuilder()
                .title("Test")
                .director("Test")
                .build();

        List<Review> reviews = new ArrayList<>();

        when(reviewRepository.findByMovie(any(Movie.class))).thenReturn(reviews);
        float result = service.calculateAverageRating(newMovie);

        assertEquals(0f,result);
    }
}
