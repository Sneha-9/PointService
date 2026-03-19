package com.sneha.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sneha.exception.InvalidUserException;
import com.sneha.exception.SystemException;
import com.sneha.exception.ValidationException;
import com.sneha.model.PointDao;
import com.sneha.pointservice.Point;
import com.sneha.pointservice.UserPointData;
import com.sneha.store.PointRepository;
import com.sneha.userservice.UserValidationRequest;
import com.sneha.userservice.UserValidationResponse;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
//@RequiredArgsConstructor
public class PointService {

    private final OkHttpClient client;

    private PointRepository pointRepository;

    private ObjectMapper objectMapper;

    @Value("${userservice.host}")
    private String userServiceHost;

    // You can also provide a default value if the property is missing
    @Value("${userservice.validation.path}")
    private String validateUserPath;

    public int aggregatePoint(int point, String id) throws Exception {
        if (id == null || id.isEmpty()) {
            throw new ValidationException("Id is either null or empty");
        }

        boolean isUserValid = validateUser(id);
        if (!isUserValid) {
            throw new InvalidUserException("User Id provided is not valid");
        }


        try {
            Optional<PointDao> optionalPointDao = pointRepository.findByRecordId(id);

            if (optionalPointDao.isEmpty()) {
                pointRepository.save(
                        PointDao.builder()
                                .recordId(id)
                                .aggregatedPoints(0)
                                .build()
                );
            }


              pointRepository.aggregatePoint(id,point);

               return pointRepository.findByRecordId(id).get().getAggregatedPoints();

        } catch (Exception e) {
            throw new SystemException("Something went wrong, please try again");
        }

    }

    public List<UserPointData> getUserPoint(int minPoint) throws SystemException {

        List<UserPointData> result = new ArrayList<>();

        try {
            List<PointDao> pointDao = pointRepository.findByMinPoint(minPoint);
            for (PointDao p : pointDao) {
                result.add(UserPointData.newBuilder()
                        .setPoint(Point.newBuilder().setValue(p.getAggregatedPoints()))
                        .setId(p.getRecordId()).build());
            }
            return result;
        } catch (Exception e) {
            throw new SystemException("Something went wrong, please try again");
        }

    }

    private boolean validateUser(String id) throws Exception {
        String url = String.format("%s/%s", userServiceHost, validateUserPath);

        //System.out.println(url);

        UserValidationRequest userValidationRequest = UserValidationRequest.newBuilder()
                .setId(id)
                .build();

        String rawRequest = objectMapper.writeValueAsString(userValidationRequest);
        RequestBody requestBody = RequestBody.create(
                rawRequest,
                MediaType.parse("application/json")
        );

        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();
        try {
            Response response = client.newCall(request).execute();
            ResponseBody responseBody = response.body();
            String rawResponse = responseBody.string();

           // System.out.printf("EBEBEEEBE %s%n", rawResponse);

            UserValidationResponse validationResponse = objectMapper.readValue(
                    rawResponse,
                    UserValidationResponse.class
            );

            return validationResponse.getIsValid();
        } catch (Exception e) {
           // e.printStackTrace();
            throw new SystemException("Something went wrong in User Service, please try again later");
        }
    }
}
