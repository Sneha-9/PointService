import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.junit5.WireMockExtension;
import com.sneha.Main;
import com.sneha.model.ErrorResponse;
import com.sneha.pointservice.*;
import com.sneha.userservice.UserValidationResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.resttestclient.autoconfigure.AutoConfigureRestTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.client.RestTestClient;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;

@SpringBootTest(
        classes = Main.class,
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
@Testcontainers
@AutoConfigureRestTestClient
public class E2ETest {

    @Autowired
    private ObjectMapper objectMapper;

    @RegisterExtension
    static WireMockExtension wireMockServer = WireMockExtension.newInstance()
            .options(wireMockConfig().dynamicPort()) // Configures a random, dynamic port
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

//        registry.add("userservice.host", () -> System.getProperty("wiremock.server.baseUrl"));
        registry.add("userservice.host", () -> wireMockServer.baseUrl());
    }


    @Test
    void shouldValidateUserAsTrue() throws IOException {
        String id = "123";
        int point = 50;

        UserValidationResponse validationResponse = UserValidationResponse.newBuilder()
                .setIsValid(true)
                .build();

        wireMockServer.stubFor(
                post("/user/validation").willReturn(
                        ok(
                                objectMapper.writeValueAsString(validationResponse)
                        ))
        );
        UserPointAggregationRequest aggregationRequest = UserPointAggregationRequest.newBuilder()
                .setId(id)
                .setPoint(point)
                .build();

        RestTestClient.ResponseSpec responseSpec = restTestClient.post()
                .uri("/point/aggregator/user")
                .contentType(MediaType.APPLICATION_JSON)
                .body(aggregationRequest)
                .exchange();

        responseSpec.expectStatus().isOk();

        UserPointAggregationResponse aggregationResponse = responseSpec.expectBody(UserPointAggregationResponse.class)
                .returnResult()
                        .getResponseBody();

        Assertions.assertNotNull(aggregationResponse);
        Assertions.assertEquals(50,aggregationResponse.getAggregatedPoint());
    }

    @Test
    void shouldThrowInvalidUserException() throws IOException {
        String id = "123";
        int point = 50;

        UserValidationResponse validationResponse = UserValidationResponse.newBuilder()
                .setIsValid(false)
                .build();

        wireMockServer.stubFor(
                post("/user/validation").willReturn(
                        ok(
                                objectMapper.writeValueAsString(validationResponse)
                        ))
        );
        UserPointAggregationRequest aggregationRequest = UserPointAggregationRequest.newBuilder()
                .setId(id)
                .setPoint(point)
                .build();

        RestTestClient.ResponseSpec responseSpec = restTestClient.post()
                .uri("/point/aggregator/user")
                .contentType(MediaType.APPLICATION_JSON)
                .body(aggregationRequest)
                .exchange();

        responseSpec.expectStatus().is5xxServerError();

        ErrorResponse errorResponse = responseSpec.expectBody(ErrorResponse.class)
                .returnResult()
                .getResponseBody();

        Assertions.assertNotNull(errorResponse);
        Assertions.assertEquals("User Id provided is not valid",errorResponse.getMessage());
    }


    @Test
    void shouldReturnListOfUsers() throws IOException {
        String id = "123";
        int point = 50;

        UserValidationResponse validationResponse = UserValidationResponse.newBuilder()
                .setIsValid(true)
                .build();

        wireMockServer.stubFor(
                post("/user/validation").willReturn(
                        ok(
                                objectMapper.writeValueAsString(validationResponse)
                        ))
        );
        UserPointAggregationRequest aggregationRequest = UserPointAggregationRequest.newBuilder()
                .setId(id)
                .setPoint(point)
                .build();

        RestTestClient.ResponseSpec responseSpec = restTestClient.post()
                .uri("/point/aggregator/user")
                .contentType(MediaType.APPLICATION_JSON)
                .body(aggregationRequest)
                .exchange();

        responseSpec.expectStatus().isOk();

        UserPointAggregationResponse aggregationResponse = responseSpec.expectBody(UserPointAggregationResponse.class)
                .returnResult()
                .getResponseBody();

        GetUserPointRequest getUserPointRequest = GetUserPointRequest.newBuilder()
                .setMinPoint(point)
                .build();

        List<UserPointData> expectedUsersResponse = new ArrayList<>();
        expectedUsersResponse.add(UserPointData
                .newBuilder()
                        .setId(id)
                .setPoint(Point.newBuilder().setValue(aggregationResponse.getAggregatedPoint()).build())
                .build());

        RestTestClient.ResponseSpec getUsersResponseSpec = restTestClient.post()
                .uri("/point/users")
                .contentType(MediaType.APPLICATION_JSON)
                .body(getUserPointRequest)
                .exchange();

        GetUserPointResponse getUserPointResponseResponse = getUsersResponseSpec.expectBody(GetUserPointResponse.class)
                .returnResult()
                .getResponseBody();
        getUsersResponseSpec.expectStatus().isOk();
        Assertions.assertEquals(expectedUsersResponse, getUserPointResponseResponse.getPointsList());

    }
}
