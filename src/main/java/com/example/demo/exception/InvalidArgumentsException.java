package com.example.demo.exception;

public class InvalidArgumentsException extends Exception {
    public InvalidArgumentsException(String errorMessage){
        super(errorMessage);
    }
}
