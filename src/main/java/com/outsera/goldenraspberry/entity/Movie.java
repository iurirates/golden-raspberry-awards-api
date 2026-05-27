package com.outsera.goldenraspberry.entity;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name = "movie")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Movie {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "release_year", nullable = false)
    private Integer year;

    @Column(nullable = false)
    private String title;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "movie_studio", joinColumns = @JoinColumn(name = "movie_id"))
    @Column(name = "studio")
    private Set<String> studios = new LinkedHashSet<>();

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "movie_producer",
            joinColumns = @JoinColumn(name = "movie_id"),
            inverseJoinColumns = @JoinColumn(name = "producer_id"))
    private Set<Producer> producers = new LinkedHashSet<>();

    @Column(nullable = false)
    private boolean winner;

    public Movie(Integer year, String title, Set<String> studios, Set<Producer> producers, boolean winner) {
        this.year = year;
        this.title = title;
        this.studios = studios != null ? new LinkedHashSet<>(studios) : new LinkedHashSet<>();
        this.producers = producers != null ? new LinkedHashSet<>(producers) : new LinkedHashSet<>();
        this.winner = winner;
    }
}
