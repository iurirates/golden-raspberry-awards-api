package com.outsera.goldenraspberry.loader;

import com.outsera.goldenraspberry.parser.domain.ParsedMovie;

import java.util.List;

public interface MovieImporter {
    int importMovies(List<ParsedMovie> movies);
}
