package com.example.demo.exception;

import com.example.demo.model.Car;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(CarNotFoundException.class)
    public ResponseEntity<String> handleCarNotFoundException(CarNotFoundException e){
        return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
    }

//
//    @ExceptionHandler(Exception.class)
//    public ResponseEntity<String> handleGlobalException(Exception e) {
//        return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
//    }
}
