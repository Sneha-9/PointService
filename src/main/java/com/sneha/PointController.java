package com.sneha;

import com.sneha.pointservice.*;
import com.google.gson.Gson;
import okhttp3.OkHttpClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
public class PointController {

    private TimeUtil timeUtil= new TimeUtil();
    private OkHttpClient okHttp = new OkHttpClient();
    private  Gson gson = new Gson();

    private PointDatabase pointDatabase = new PointDatabase();

    private PointService pointService= new PointService(timeUtil,okHttp,gson,pointDatabase);

   @PostMapping(value = "/point/aggregator/user", produces = "application/json")
    UserPointAggregationResponse aggregateUserPoint(UserPointAggregationRequest userPointAggregationRequest){
       int result =  pointService.aggregatePoint(userPointAggregationRequest.getPoint(),userPointAggregationRequest.getId());
       return   UserPointAggregationResponse.newBuilder().setAggregatedPoint(result).build();
   }

   @PostMapping(value= "/point/users", produces = "application/json")
    GetUserPointResponse getUserPoints(GetUserPointRequest getUserPointRequest){
      List<UserPointData> result = pointService.getUserPoint(getUserPointRequest.getMinPoint());
      return  GetUserPointResponse.newBuilder().addAllPoints(result).build();
   }





}
