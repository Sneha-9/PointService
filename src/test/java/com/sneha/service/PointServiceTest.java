package com.sneha.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sneha.exception.ValidationException;
import com.sneha.store.PointRepository;
import okhttp3.OkHttpClient;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class PointServiceTest {
    private PointRepository pointRepository = mock(PointRepository.class);
    private OkHttpClient okHttpClient= mock(OkHttpClient.class);
    private ObjectMapper objectMapper = mock(ObjectMapper.class);
    @Test
    void shouldReturnExceptionWhenIdIsNull(){

    }

}