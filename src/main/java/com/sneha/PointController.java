package com.sneha;

import com.sneha.exception.SystemException;
import com.sneha.pointservice.*;
import com.sneha.service.PointService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;


@RestController
@AllArgsConstructor
public class PointController {

    private PointService pointService;

   @PostMapping(value = Constant.AGGREGATE_POINT_PATH, produces = Constant.JSON_RESPONSE_MEDIA_TYPE)
    UserPointAggregationResponse aggregateUserPoint(@RequestBody UserPointAggregationRequest userPointAggregationRequest) throws Exception {
       int result =  pointService.aggregatePoint(userPointAggregationRequest.getPoint(),userPointAggregationRequest.getId());
       return   UserPointAggregationResponse.newBuilder().setAggregatedPoint(result).build();
   }

   @PostMapping(value= Constant.GET_POINTS_PATH , produces = Constant.JSON_RESPONSE_MEDIA_TYPE)
    GetUserPointResponse getUserPoints(@RequestBody GetUserPointRequest getUserPointRequest) throws SystemException {
      List<UserPointData> result = pointService.getUserPoint(getUserPointRequest.getMinPoint());

      return  GetUserPointResponse.newBuilder().addAllPoints(result).build();

   }





}
