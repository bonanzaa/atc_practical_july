package com.example.demo.controller;

import com.example.demo.model.Movie;
import com.example.demo.model.Review;
import com.example.demo.repository.MovieRepository;
import com.example.demo.repository.ReviewRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class ReviewControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private MovieRepository movieRepository;

    @Test
    public void addReview() throws Exception {
        Movie newMovie = new Movie().toBuilder()
                .id(1L)
                .title("Movie1")
                .director("director")
                .build();

        movieRepository.save(newMovie);

        Review newReview = new Review().toBuilder()
                .id(1L)
                .username("asd")
                .reviewText("asd")
                .rating(1)
                .build();

        mockMvc.perform(post("/reviews/new/1")
                        .contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(newReview)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isNotEmpty());
    }

    @Test
    public void editReview() throws Exception {
        Movie newMovie = new Movie().toBuilder()
                .id(1L)
                .title("Movie1")
                .director("director")
                .build();

        movieRepository.save(newMovie);

        Review existingReview = new Review().toBuilder()
                .id(1L)
                .username("asd")
                .reviewText("asd")
                .rating(1)
                .movie(newMovie)
                .build();
        reviewRepository.save(existingReview);

        Review newReview = new Review().toBuilder()
                .id(1L)
                .username("asd")
                .reviewText("asd")
                .rating(10)
                .movie(newMovie)
                .build();

        mockMvc.perform(post("/reviews/edit/1")
                        .contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(newReview)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.rating").value("10"));
    }
}
