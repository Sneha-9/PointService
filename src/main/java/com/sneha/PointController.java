package com.sneha;

import com.sneha.pointservice.*;
import com.google.gson.Gson;
import lombok.AllArgsConstructor;
import okhttp3.OkHttpClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;


@RestController
@AllArgsConstructor
public class PointController {

    private PointService pointService;

   @PostMapping(value = "/point/aggregator/user", produces = "application/json")
    UserPointAggregationResponse aggregateUserPoint(@RequestBody UserPointAggregationRequest userPointAggregationRequest) throws Exception {
       int result =  pointService.aggregatePoint(userPointAggregationRequest.getPoint(),userPointAggregationRequest.getId());
       return   UserPointAggregationResponse.newBuilder().setAggregatedPoint(result).build();
   }

   @PostMapping(value= "/point/users", produces = "application/json")
    GetUserPointResponse getUserPoints(@RequestBody GetUserPointRequest getUserPointRequest){
      List<UserPointData> result = pointService.getUserPoint(getUserPointRequest.getMinPoint());

      return  GetUserPointResponse.newBuilder().addAllPoints(result).build();

   }





}
