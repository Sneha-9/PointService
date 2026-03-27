package com.sneha.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sneha.ConfigProperties;
import com.sneha.Constant;
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

import lombok.extern.slf4j.Slf4j;
import okhttp3.*;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@AllArgsConstructor

public class PointService {

    private final OkHttpClient client;
    private PointRepository pointRepository;
    private ObjectMapper objectMapper;
    private ConfigProperties configProperties;


    public int aggregatePoint(int point, String id) throws ValidationException, InvalidUserException, SystemException, JsonProcessingException {

        if (id == null || id.isEmpty()) {
            throw new ValidationException(Constant.ID_EXCEPTION_MESSAGE);
        }

        boolean isUserValid = validateUser(id);
        if (!isUserValid) {
            throw new InvalidUserException(Constant.USER_ID_EXCEPTION_MESSAGE);
        }

        Optional<PointDao> optionalPointDao;
        try {
            optionalPointDao = pointRepository.findByRecordId(id);
        }
        catch (Exception e) {
            log.error("Error in Point Service while fetching record by id",e);
            throw new SystemException(Constant.SYSTEM_EXCEPTION_MESSAGE);
        }
        try {
            if (optionalPointDao.isEmpty()) {
                pointRepository.save(
                        PointDao.builder()
                                .recordId(id)
                                .aggregatedPoints(0)
                                .build()
                );
            }
        }
        catch (Exception e) {
            log.error("Error in Point Service while inserting the record", e);
            throw new SystemException(Constant.SYSTEM_EXCEPTION_MESSAGE);
        }
             try {
                 pointRepository.aggregatePoint(id, point);
             }
             catch (Exception e) {
                 log.error("Error in Point Service while updating the record", e);
                 throw new SystemException(Constant.SYSTEM_EXCEPTION_MESSAGE);
             }
             try{
               return pointRepository.findByRecordId(id).get().getAggregatedPoints();

        } catch (Exception e) {
            log.error("Error in Point Service while finding the aggregated point of a record",e);
            throw new SystemException(Constant.SYSTEM_EXCEPTION_MESSAGE);
        }

    }

    public List<UserPointData> getUserPoint(int minPoint) throws SystemException {
        List<UserPointData> result = new ArrayList<>();
        List<PointDao> pointDao;

        try {
             pointDao = pointRepository.findByMinPoint(minPoint);
        } catch (Exception e) {
            log.error("Error while finding the record based on minimum point", e);
            throw new SystemException(Constant.SYSTEM_EXCEPTION_MESSAGE);
        }
        for (PointDao p : pointDao) {
            result.add(UserPointData.newBuilder()
                    .setPoint(Point.newBuilder().setValue(p.getAggregatedPoints()))
                    .setId(p.getRecordId()).build());
        }
        return result;

    }

    private boolean validateUser(String id) throws SystemException, JsonProcessingException {
        String url = "http://"+ configProperties.getUserServiceConfig().getHost() + configProperties.getUserServiceConfig().getPath();

        UserValidationRequest userValidationRequest = UserValidationRequest.newBuilder()
                .setId(id)
                .build();

        String rawRequest = objectMapper.writeValueAsString(userValidationRequest);
        RequestBody requestBody = RequestBody.create(
                rawRequest,
                MediaType.parse(Constant.JSON_RESPONSE_MEDIA_TYPE)
        );

        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();
        try {
            Response response = client.newCall(request).execute();
            ResponseBody responseBody = response.body();
            String rawResponse = responseBody.string();

            UserValidationResponse validationResponse = objectMapper.readValue(
                    rawResponse,
                    UserValidationResponse.class
            );

            return validationResponse.getIsValid();
        } catch (Exception e) {
      log.error("Error while calling user service, e");
            throw new SystemException(Constant.SYSTEM_EXCEPTION_MESSAGE);
        }
    }
}
