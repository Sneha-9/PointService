package com.sneha;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.protobuf.util.JsonFormat;
import com.sneha.model.PointDao;
import com.sneha.pointservice.Point;
import com.sneha.pointservice.UserPointData;
import com.sneha.store.PointRepository;
import com.sneha.userservice.UserValidationRequest;
import com.sneha.userservice.UserValidationResponse;
import lombok.AllArgsConstructor;
import okhttp3.*;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class PointService {

    //private PointDatabase pointDatabase;
    private final OkHttpClient client;

//    private final Gson gson;

    private PointRepository pointRepository;

    private ObjectMapper objectMapper;

    int aggregatePoint(int point, String id) throws Exception {
        boolean isUserValid = validateUser(id);
        if (!isUserValid) {
            throw new IllegalArgumentException("User invalid");
        }

        Optional<PointDao> optionalPointDao = pointRepository.findByRecordId(id);

        if (optionalPointDao.isEmpty()) {
            pointRepository.save(
                    PointDao.builder()
                            .recordId(id)
                            .aggregatedPoints(0)
                            .build()
            );
        }

        int noOfRows = pointRepository.aggregatePoint(id, point);

        return 0;

    }

    List<UserPointData> getUserPoint(int minPoint) {

        List<UserPointData> result = new ArrayList<>();

        List<PointDao> pointDao = pointRepository.findByMinPoint(minPoint);
        for (PointDao p : pointDao) {
            result.add(UserPointData.newBuilder()
                    .setPoint(Point.newBuilder().setValue(p.getAggregatedPoints()))
                    .setId(p.getRecordId()).build());
        }
        return result;

    }

    private boolean validateUser(String id) throws Exception {
        String url = "http://localhost:8090/user/validation";

        UserValidationRequest userValidationRequest = UserValidationRequest.newBuilder()
                .setId(id)
                .build();

        String rawRequest = objectMapper.writeValueAsString(userValidationRequest);

        System.out.printf("%n Mr.Nikhil Soni -1 %s %n", rawRequest);

        RequestBody requestBody = RequestBody.create(
                rawRequest,
                MediaType.parse("application/json")
        );

        System.out.printf("%n Mr.Nikhil Soni 0 %s %n", requestBody);

        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();
        try {
            Response response = client.newCall(request).execute();

            ResponseBody responseBody = response.body();

            String rawResponse = responseBody.string();

            System.out.printf("%n Mr.Nikhil Soni 1 %s %n", response);
            System.out.printf("%n Mr.Nikhil Soni 2 %s %n", responseBody);
            System.out.printf("%n Mr.Nikhil Soni 3 %s %n", rawResponse);

            UserValidationResponse validationResponse = objectMapper.readValue(
                    rawResponse,
                    UserValidationResponse.class
            );

            System.out.printf("%n Mr.Nikhil Soni 4 %s %n", validationResponse.getIsValid());

            return validationResponse.getIsValid();
        } catch (Exception e) {
            throw new Exception("There was issue while calling User Identification Service", e);
        }
    }
}
