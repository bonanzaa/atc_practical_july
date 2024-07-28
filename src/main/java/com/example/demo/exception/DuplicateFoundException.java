package com.example.demo.exception;

public class DuplicateFoundException extends Exception{
    public DuplicateFoundException(String errorMessage){
        super(errorMessage);
    }
}
