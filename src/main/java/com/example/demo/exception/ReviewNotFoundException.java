package com.example.demo.exception;

public class ReviewNotFoundException extends Exception{
    public ReviewNotFoundException(String errorMessage){
        super(errorMessage);
    }
}
