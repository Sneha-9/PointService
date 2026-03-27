package com.sneha.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sneha.ConfigProperties;
import com.sneha.Constant;
import com.sneha.TestConstant;
import com.sneha.exception.SystemException;
import com.sneha.exception.ValidationException;
import com.sneha.store.PointRepository;
import com.sneha.userservice.UserService;
import com.sneha.userservice.UserValidationResponse;
import okhttp3.OkHttpClient;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PointServiceTest {
    private PointRepository pointRepository = mock(PointRepository.class);
    private OkHttpClient okHttpClient= mock(OkHttpClient.class);
    private ObjectMapper objectMapper = mock(ObjectMapper.class);
    private ConfigProperties configProperties ;

        @Test
        void shouldReturnExceptionWhenIdIsEmpty(){
          Assertions.assertThrows(ValidationException.class,()-> new PointService(okHttpClient,pointRepository,objectMapper, configProperties).aggregatePoint(TestConstant.POINTS, ""));
        }
        @Test
        void shouldReturnExceptionWhenIdIsNull(){
            Assertions.assertThrows(ValidationException.class,()-> new PointService(okHttpClient,pointRepository,objectMapper, configProperties).aggregatePoint(TestConstant.POINTS, null));

        }





}