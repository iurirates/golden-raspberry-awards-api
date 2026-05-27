package com.outsera.goldenraspberry.domain;

import lombok.With;

@With
public record ProducerInterval(
        String producer,
        int interval,
        int previousWin,
        int followingWin) {
}
