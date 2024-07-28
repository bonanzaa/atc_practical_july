package com.example.demo.controller;

import com.example.demo.model.Review;
import com.example.demo.model.ReviewDTO;
import com.example.demo.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("reviews")
public class ReviewController {
    @Autowired
    private ReviewService reviewService;

    @PostMapping("/new/{id}")
    public ResponseEntity<Review> addReview(@RequestBody Review review, @PathVariable Long id) throws Exception {
        Review result = reviewService.addNewReview(review,id);
        return new ResponseEntity<>(result,HttpStatus.OK);
    }

    @PostMapping("/edit/{id}")
    public ResponseEntity<Review> editReview(@RequestBody Review review, @PathVariable Long id) throws Exception {
        Review result = reviewService.editExistingReview(review,id);
        return new ResponseEntity<>(result,HttpStatus.OK);
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<List<ReviewDTO>> getReviewsForMovie(@PathVariable Long id){
         List<ReviewDTO> result = reviewService.getReviewsForMovieId(id);
        return new ResponseEntity<>(result,HttpStatus.OK);
    }
    @GetMapping("/user/{username}")
    public ResponseEntity<List<Review>> getReviewsForUsername(@PathVariable String username){
        List<Review> result = reviewService.getReviewsByUsername(username.toLowerCase());
        return new ResponseEntity<>(result,HttpStatus.OK);
    }
}
