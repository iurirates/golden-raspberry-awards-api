package com.outsera.goldenraspberry.parser;

import com.outsera.goldenraspberry.parser.domain.ParsedMovie;

import java.io.InputStream;
import java.util.List;

public interface MovieParser {
    List<ParsedMovie> parse(InputStream inputStream);
}
