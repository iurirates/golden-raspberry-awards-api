package com.outsera.goldenraspberry.controller;

import com.outsera.goldenraspberry.controller.api.MovieApi;
import com.outsera.goldenraspberry.controller.domain.request.MovieRequest;
import com.outsera.goldenraspberry.controller.domain.response.MovieResponse;
import com.outsera.goldenraspberry.service.MovieService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class MovieController implements MovieApi {

    private final MovieService movieService;

    @Override
    public ResponseEntity<List<MovieResponse>> findAll() {
        return ResponseEntity.ok(movieService.findAll());
    }

    @Override
    public ResponseEntity<MovieResponse> findById(Long id) {
        return ResponseEntity.ok(movieService.findById(id));
    }

    @Override
    public ResponseEntity<MovieResponse> create(MovieRequest request, UriComponentsBuilder uriBuilder) {
        MovieResponse created = movieService.create(request);
        URI location = uriBuilder.path("/api/movies/{id}").buildAndExpand(created.id()).toUri();
        return ResponseEntity.created(location).body(created);
    }

    @Override
    public ResponseEntity<MovieResponse> update(Long id, MovieRequest request) {
        return ResponseEntity.ok(movieService.update(id, request));
    }

    @Override
    public ResponseEntity<Void> delete(Long id) {
        movieService.delete(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
