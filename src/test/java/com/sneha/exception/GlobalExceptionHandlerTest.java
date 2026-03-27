package com.sneha.exception;

import com.sneha.Constant;
import com.sneha.errorservice.ErrorResponse;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

class GlobalExceptionHandlerTest {

    GlobalExceptionHandler handler = new GlobalExceptionHandler();


    @Test
    void shouldTestValidationException(){
        ValidationException validationException = new ValidationException(Constant.ID_EXCEPTION_MESSAGE);

        ErrorResponse errorResponse = ErrorResponse.newBuilder()
                .setMessage(Constant.ID_EXCEPTION_MESSAGE)
                .build();

        ResponseEntity<ErrorResponse> responseEntity = handler.handleValidationException(validationException);

        Assertions.assertNotNull(responseEntity);
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        Assertions.assertEquals(errorResponse, responseEntity.getBody());
    }

    @Test
    void shouldTestSystemException(){
        SystemException systemException = new SystemException(Constant.SYSTEM_EXCEPTION_MESSAGE);

        ErrorResponse errorResponse = ErrorResponse.newBuilder()
                .setMessage(Constant.SYSTEM_EXCEPTION_MESSAGE)
                .build();

        ResponseEntity<ErrorResponse> responseEntity = handler.handleSystemException(systemException);

        Assertions.assertNotNull(responseEntity);
        Assertions.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
        Assertions.assertEquals(errorResponse, responseEntity.getBody());
    }

    @Test
    void shouldTestInvalidUserException(){
        InvalidUserException invalidUserException = new InvalidUserException(Constant.USER_ID_EXCEPTION_MESSAGE);

        ErrorResponse errorResponse = ErrorResponse.newBuilder()
                .setMessage(Constant.USER_ID_EXCEPTION_MESSAGE)
                .build();

        ResponseEntity<ErrorResponse> responseEntity = handler.handleInvalidUserException(invalidUserException);

        Assertions.assertNotNull(responseEntity);
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        Assertions.assertEquals(errorResponse, responseEntity.getBody());
    }

}