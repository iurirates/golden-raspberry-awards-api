package com.outsera.goldenraspberry.controller;

import com.outsera.goldenraspberry.controller.api.ProducerApi;
import com.outsera.goldenraspberry.controller.domain.response.AwardIntervalsResponse;
import com.outsera.goldenraspberry.service.AwardIntervalService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ProducerController implements ProducerApi {

    private final AwardIntervalService awardIntervalService;

    @Override
    public ResponseEntity<AwardIntervalsResponse> getAwardIntervals() {
        return ResponseEntity.ok(awardIntervalService.calculateAwardIntervals());
    }
}
