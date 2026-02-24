package com.sneha;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.hubspot.jackson.datatype.protobuf.ProtobufModule;
import okhttp3.OkHttpClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.protobuf.ProtobufJsonFormatHttpMessageConverter;

@Configuration
public class AppConfig {

    @Bean
    public OkHttpClient getOkHttpClient() {
        return new OkHttpClient.Builder().build();
    }

    @Bean
    public ObjectMapper getObjectMapper() {
        return JsonMapper.builder()
                .addModule(new ProtobufModule())
                .propertyNamingStrategy(PropertyNamingStrategies.LOWER_CAMEL_CASE) // Force camelCase
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                .build();
    }

    // This bean tells Spring how to turn JSON into Protobuf objects
    @Bean
    public ProtobufJsonFormatHttpMessageConverter protobufHttpMessageConverter() {
        return new ProtobufJsonFormatHttpMessageConverter();
    }
}