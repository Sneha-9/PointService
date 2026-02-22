package com.sneha;

import com.google.gson.Gson;
import java.util.List;

import com.sneha.pointservice.Point;
import com.sneha.pointservice.UserPointData;
import com.sneha.userservice.UserValidationResponse;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class PointService {

     private PointDatabase pointDatabase;
     private final OkHttpClient client ;
     private TimeUtil timeUtil;
     private final Gson gson;
     PointService(TimeUtil timeUtil,OkHttpClient client, Gson gson, PointDatabase pointDatabase){
        this.client = client;
        this.gson =gson;
        this.pointDatabase = pointDatabase;
        this.timeUtil = timeUtil;
     }

    int aggregatePoint(int point, String id){

     Point aggregatePoint =   pointDatabase.aggregate(id,point,timeUtil.getCurrentTime(), timeUtil.getCurrentTime());
      return aggregatePoint.getValue();

    }
    List<UserPointData> getUserPoint(int minPoint){

      List<UserPointData> result = pointDatabase.getUserPoints(minPoint);
      return result;

    }
    private boolean validateUser(String id) throws Exception {
        String url = String.format("http://localhost:8090/user/validation", id);

        Request request = new Request.Builder()
                .url(url)
                .build();
        try {
            Response response = client.newCall(request).execute();
            UserValidationResponse validationResponse = gson.fromJson(response.body().string(), UserValidationResponse.class);
            return validationResponse.getIsValid();
        } catch (Exception e) {
            throw new Exception("There was issue while calling User Identification Service");
        }
    }
}
