package com.outsera.goldenraspberry.service.impl;

import com.outsera.goldenraspberry.controller.domain.request.MovieRequest;
import com.outsera.goldenraspberry.controller.domain.response.MovieResponse;
import com.outsera.goldenraspberry.entity.Movie;
import com.outsera.goldenraspberry.entity.Producer;
import com.outsera.goldenraspberry.exception.ResourceNotFoundException;
import com.outsera.goldenraspberry.mapper.MovieMapper;
import com.outsera.goldenraspberry.repository.MovieRepository;
import com.outsera.goldenraspberry.repository.ProducerRepository;
import com.outsera.goldenraspberry.service.MovieService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class MovieServiceImpl implements MovieService {

    private final MovieRepository movieRepository;
    private final ProducerRepository producerRepository;
    private final MovieMapper movieMapper;

    @Override
    @Transactional(readOnly = true)
    public List<MovieResponse> findAll() {
        return movieRepository.findAll().stream()
                .map(movieMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public MovieResponse findById(Long id) {
        return movieMapper.toResponse(getMovieOrThrow(id));
    }

    @Override
    @Transactional
    public MovieResponse create(MovieRequest request) {
        Movie movie = new Movie(
                request.year(),
                request.title(),
                normalize(request.studios()),
                resolveProducers(request.producers()),
                request.winner());
        return movieMapper.toResponse(movieRepository.save(movie));
    }

    @Override
    @Transactional
    public MovieResponse update(Long id, MovieRequest request) {
        Movie movie = getMovieOrThrow(id);
        movie.setYear(request.year());
        movie.setTitle(request.title());
        movie.setStudios(normalize(request.studios()));
        movie.setProducers(resolveProducers(request.producers()));
        movie.setWinner(request.winner());
        return movieMapper.toResponse(movieRepository.save(movie));
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Movie movie = getMovieOrThrow(id);
        movieRepository.delete(movie);
    }

    private Movie getMovieOrThrow(Long id) {
        return movieRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Filme não encontrado com id " + id));
    }

    private Set<String> normalize(Set<String> values) {
        Set<String> result = new LinkedHashSet<>();
        if (values != null) {
            for (String value : values) {
                if (value != null && !value.isBlank()) {
                    result.add(value.trim());
                }
            }
        }
        return result;
    }

    private Set<Producer> resolveProducers(Set<String> producerNames) {
        Set<Producer> producers = new LinkedHashSet<>();
        for (String name : normalize(producerNames)) {
            Producer producer = producerRepository.findByName(name)
                    .orElseGet(() -> producerRepository.save(new Producer(name)));
            producers.add(producer);
        }
        return producers;
    }
}
