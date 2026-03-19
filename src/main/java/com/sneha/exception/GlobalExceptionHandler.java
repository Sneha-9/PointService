package com.sneha.exception;

import com.sneha.model.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {


    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<Object> handleValidationException(ValidationException ex) {
        ErrorResponse error = new ErrorResponse(ex.getMessage());

        return new ResponseEntity<>(error,  HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(SystemException.class)
    public ResponseEntity<Object> handleSystemException(SystemException ex) {
        ErrorResponse error = new ErrorResponse(ex.getMessage());

        return new ResponseEntity<>(error,  HttpStatus.INTERNAL_SERVER_ERROR);
    }
    @ExceptionHandler(InvalidUserException.class)
    public ResponseEntity<Object> handleInvalidUserException(InvalidUserException ex) {
        ErrorResponse error = new ErrorResponse(ex.getMessage());

        return new ResponseEntity<>(error,  HttpStatus.INTERNAL_SERVER_ERROR);
    }



}
