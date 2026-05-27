package com.outsera.goldenraspberry.loader;

import com.outsera.goldenraspberry.parser.domain.ParsedMovie;
import com.outsera.goldenraspberry.entity.Movie;
import com.outsera.goldenraspberry.entity.Producer;
import com.outsera.goldenraspberry.repository.MovieRepository;
import com.outsera.goldenraspberry.repository.ProducerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class DefaultMovieImporter implements MovieImporter {

    private final MovieRepository movieRepository;
    private final ProducerRepository producerRepository;

    @Override
    @Transactional
    public int importMovies(List<ParsedMovie> movies) {
        Map<String, Producer> producerCache = new HashMap<>();

        for (ParsedMovie parsedMovie : movies) {
            Set<Producer> producers = new LinkedHashSet<>();
            for (String name : parsedMovie.producers()) {
                Producer producer = producerCache.computeIfAbsent(
                        name,
                        n -> producerRepository.findByName(n).orElseGet(() -> producerRepository.save(new Producer(n))));
                producers.add(producer);
            }

            Movie movie = new Movie(
                    parsedMovie.year(),
                    parsedMovie.title(),
                    parsedMovie.studios(),
                    producers,
                    parsedMovie.winner());
            movieRepository.save(movie);
        }

        return movies.size();
    }
}
