package com.outsera.goldenraspberry.service.impl;

import com.outsera.goldenraspberry.controller.domain.response.AwardIntervalsResponse;
import com.outsera.goldenraspberry.domain.ProducerInterval;
import com.outsera.goldenraspberry.entity.Movie;
import com.outsera.goldenraspberry.entity.Producer;
import com.outsera.goldenraspberry.repository.MovieRepository;
import com.outsera.goldenraspberry.service.AwardIntervalService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.TreeSet;

@Service
@RequiredArgsConstructor
public class AwardIntervalServiceImpl implements AwardIntervalService {

    private final MovieRepository movieRepository;

    @Override
    @Transactional(readOnly = true)
    public AwardIntervalsResponse calculateAwardIntervals() {
        Map<String, TreeSet<Integer>> winsByProducer = collectWinningYearsByProducer();
        List<ProducerInterval> intervals = buildConsecutiveIntervals(winsByProducer);

        if (intervals.isEmpty()) {
            return new AwardIntervalsResponse(Collections.emptyList(), Collections.emptyList());
        }

        int minInterval = intervals.stream().mapToInt(ProducerInterval::interval).min().orElseThrow();
        int maxInterval = intervals.stream().mapToInt(ProducerInterval::interval).max().orElseThrow();

        List<ProducerInterval> min = intervals.stream()
                .filter(i -> i.interval() == minInterval)
                .toList();
        List<ProducerInterval> max = intervals.stream()
                .filter(i -> i.interval() == maxInterval)
                .toList();

        return new AwardIntervalsResponse(min, max);
    }

    private Map<String, TreeSet<Integer>> collectWinningYearsByProducer() {
        Map<String, TreeSet<Integer>> winsByProducer = new TreeMap<>();
        for (Movie movie : movieRepository.findWinnersWithProducers()) {
            for (Producer producer : movie.getProducers()) {
                winsByProducer
                        .computeIfAbsent(producer.getName(), k -> new TreeSet<>())
                        .add(movie.getYear());
            }
        }
        return winsByProducer;
    }

    private List<ProducerInterval> buildConsecutiveIntervals(Map<String, TreeSet<Integer>> winsByProducer) {
        List<ProducerInterval> intervals = new ArrayList<>();
        for (Map.Entry<String, TreeSet<Integer>> entry : winsByProducer.entrySet()) {
            String producer = entry.getKey();
            List<Integer> years = new ArrayList<>(entry.getValue());
            for (int i = 1; i < years.size(); i++) {
                int previousWin = years.get(i - 1);
                int followingWin = years.get(i);
                intervals.add(new ProducerInterval(
                        producer,
                        followingWin - previousWin,
                        previousWin,
                        followingWin));
            }
        }
        return intervals;
    }
}
