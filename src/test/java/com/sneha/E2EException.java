package com.sneha;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.junit5.WireMockExtension;
import com.sneha.errorservice.ErrorResponse;
import com.sneha.exception.SystemException;
import com.sneha.pointservice.UserPointAggregationRequest;
import com.sneha.store.PointRepository;
import com.sneha.userservice.UserValidationResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.resttestclient.autoconfigure.AutoConfigureRestTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.client.RestTestClient;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static com.github.tomakehurst.wiremock.client.WireMock.ok;
import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
import static org.mockito.Mockito.doThrow;
@SpringBootTest(
        classes = Main.class,
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
@Testcontainers
@AutoConfigureRestTestClient
public class E2EException {
    @MockitoBean
    private PointRepository pointRepository;
    @Autowired
    private ObjectMapper objectMapper;

    @RegisterExtension
    static WireMockExtension wireMockServer = WireMockExtension.newInstance()
            .options(wireMockConfig().dynamicPort())
            .build();

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:18")
            .withDatabaseName("pointservice")
            .withUsername("sneha")
            .withPassword("password")
            .withInitScript("test-init.sql");

    @Autowired
    private RestTestClient restTestClient;

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);

        registry.add("userservice.host", () -> wireMockServer.baseUrl().replace("http://",""));
    }

    @AfterEach
    void resetMocks() {
        Mockito.reset(pointRepository);
    }

    @Test
    void shouldTestSystemExceptionFindByRecordId() throws Exception {
        String id = TestConstant.ID;
        int point = TestConstant.POINTS;

        UserValidationResponse validationResponse = UserValidationResponse.newBuilder()
                .setIsValid(true)
                .build();

        wireMockServer.stubFor(
                post(TestConstant.USER_VALIDATION_PATH).willReturn(
                        ok(
                                objectMapper.writeValueAsString(validationResponse)
                        ))
        );

        doThrow(SystemException.class).when(pointRepository).findByRecordId(TestConstant.ID);

        UserPointAggregationRequest aggregationRequest = UserPointAggregationRequest.newBuilder()
                .setId(id)
                .setPoint(point)
                .build();

        RestTestClient.ResponseSpec responseSpec = restTestClient.post()
                .uri(Constant.AGGREGATE_POINT_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .body(aggregationRequest)
                .exchange();

        ErrorResponse errorResponse = responseSpec.expectBody(ErrorResponse.class)
                .returnResult()
                .getResponseBody();

        Assertions.assertNotNull(errorResponse);
        Assertions.assertEquals(Constant.SYSTEM_EXCEPTION_MESSAGE,errorResponse.getMessage());
    }
    @Test
    void shouldTestSystemExceptionAggregatePoint() throws Exception {
        String id = TestConstant.ID;
        int point = TestConstant.POINTS;

        UserValidationResponse validationResponse = UserValidationResponse.newBuilder()
                .setIsValid(true)
                .build();

        wireMockServer.stubFor(
                post(TestConstant.USER_VALIDATION_PATH).willReturn(
                        ok(
                                objectMapper.writeValueAsString(validationResponse)
                        ))
        );

        doThrow(SystemException.class).when(pointRepository).aggregatePoint(id,point);

        UserPointAggregationRequest aggregationRequest = UserPointAggregationRequest.newBuilder()
                .setId(id)
                .setPoint(point)
                .build();

        RestTestClient.ResponseSpec responseSpec = restTestClient.post()
                .uri(Constant.AGGREGATE_POINT_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .body(aggregationRequest)
                .exchange();

        ErrorResponse errorResponse = responseSpec.expectBody(ErrorResponse.class)
                .returnResult()
                .getResponseBody();

        Assertions.assertNotNull(errorResponse);
        Assertions.assertEquals(Constant.SYSTEM_EXCEPTION_MESSAGE,errorResponse.getMessage());
    }

    @Test
    void shouldTestSystemExceptionFindByMinPoint() throws Exception {
        String id = TestConstant.ID;
        int point = TestConstant.POINTS;

        UserValidationResponse validationResponse = UserValidationResponse.newBuilder()
                .setIsValid(true)
                .build();

        wireMockServer.stubFor(
                post(TestConstant.USER_VALIDATION_PATH).willReturn(
                        ok(
                                objectMapper.writeValueAsString(validationResponse)
                        ))
        );

        doThrow(SystemException.class).when(pointRepository).findByMinPoint(point);

        UserPointAggregationRequest aggregationRequest = UserPointAggregationRequest.newBuilder()
                .setId(id)
                .setPoint(point)
                .build();

        RestTestClient.ResponseSpec responseSpec = restTestClient.post()
                .uri(Constant.AGGREGATE_POINT_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .body(aggregationRequest)
                .exchange();

        ErrorResponse errorResponse = responseSpec.expectBody(ErrorResponse.class)
                .returnResult()
                .getResponseBody();

        Assertions.assertNotNull(errorResponse);
        Assertions.assertEquals(Constant.SYSTEM_EXCEPTION_MESSAGE,errorResponse.getMessage());
    }

}
