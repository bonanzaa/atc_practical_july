package com.example.demo.repository;

import com.example.demo.model.Movie;
import com.example.demo.model.Review;
import com.example.demo.model.ReviewDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReviewRepository extends JpaRepository<Review,Long> {
    List<Review> findByMovie(Movie movie);

    @Query(nativeQuery = true,value="SELECT * FROM review WHERE movie_id=?1")
    List<Review> findByMovieId(Long id);

    @Query(nativeQuery = true,value="SELECT * FROM review WHERE username=?1")
    List<Review> findByUsername(String username);

    @Query(nativeQuery = true,value="SELECT * FROM review WHERE username=?1 AND movie_id=?2")
    Review findExistingReviews(String username,Long id);
}
