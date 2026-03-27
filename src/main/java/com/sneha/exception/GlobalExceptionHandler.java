package com.sneha.exception;


import com.sneha.errorservice.ErrorResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;


import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {


    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(ValidationException ex) {

        return getResponseEntity(ex,  HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(SystemException.class)
    public ResponseEntity<ErrorResponse> handleSystemException(SystemException ex) {
        return getResponseEntity(ex,  HttpStatus.INTERNAL_SERVER_ERROR);
    }
    @ExceptionHandler(InvalidUserException.class)
    public ResponseEntity<ErrorResponse> handleInvalidUserException(InvalidUserException ex) {

        return getResponseEntity(ex,  HttpStatus.BAD_REQUEST);
    }


    private ResponseEntity<ErrorResponse> getResponseEntity(Exception ex, HttpStatus status) {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        ErrorResponse error = ErrorResponse.newBuilder().setMessage(ex.getMessage()).build();

        return new ResponseEntity<>(error, headers, status);
    }
}
