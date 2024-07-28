package com.example.demo.repository;

import com.example.demo.model.Movie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MovieRepository extends JpaRepository<Movie,Long> {

    @Query(nativeQuery = true,value = "SELECT * FROM movie WHERE title = ?1 AND director = ?2")
    Optional<Movie> getMovieByTitleAndDirector(String title,String director);
}
