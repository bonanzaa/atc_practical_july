package com.example.demo.controller;

import com.example.demo.model.Movie;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class MovieControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void getAllMovies() throws Exception {
        mockMvc.perform(get("/movies/all"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    public void addMovies() throws Exception {
        List<Movie> moviesToAdd = List.of(
                new Movie().toBuilder()
                        .title("Movie1").build(),
                new Movie().toBuilder()
                        .title("Movie2").build()
        );
        mockMvc.perform(post("/movies/new")
                .contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(moviesToAdd)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());

        mockMvc.perform(get("/movies/all"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].title").value("Movie1"));
    }


}
