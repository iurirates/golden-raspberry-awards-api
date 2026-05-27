package com.outsera.goldenraspberry.controller.domain.response;

import lombok.With;

import java.util.List;

@With
public record MovieResponse(
        Long id,
        int year,
        String title,
        List<String> studios,
        List<String> producers,
        boolean winner) {
}
