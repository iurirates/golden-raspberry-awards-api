package com.outsera.goldenraspberry.repository;

import com.outsera.goldenraspberry.entity.Movie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MovieRepository extends JpaRepository<Movie, Long> {
    @Query("select distinct m from Movie m join fetch m.producers where m.winner = true order by m.year")
    List<Movie> findWinnersWithProducers();
}
