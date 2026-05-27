package com.outsera.goldenraspberry.controller.domain.response;

import com.outsera.goldenraspberry.domain.ProducerInterval;
import lombok.With;

import java.util.List;

@With
public record AwardIntervalsResponse(
        List<ProducerInterval> min,
        List<ProducerInterval> max) {
}
