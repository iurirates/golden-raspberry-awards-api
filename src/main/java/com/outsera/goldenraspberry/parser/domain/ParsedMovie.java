package com.outsera.goldenraspberry.parser.domain;

import lombok.With;

import java.util.Set;

@With
public record ParsedMovie(
        int year,
        String title,
        Set<String> studios,
        Set<String> producers,
        boolean winner) {
}
