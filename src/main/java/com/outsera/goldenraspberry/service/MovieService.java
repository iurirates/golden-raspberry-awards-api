package com.outsera.goldenraspberry.service;

import com.outsera.goldenraspberry.controller.domain.request.MovieRequest;
import com.outsera.goldenraspberry.controller.domain.response.MovieResponse;

import java.util.List;

public interface MovieService {
    List<MovieResponse> findAll();

    MovieResponse findById(Long id);

    MovieResponse create(MovieRequest request);

    MovieResponse update(Long id, MovieRequest request);

    void delete(Long id);
}
