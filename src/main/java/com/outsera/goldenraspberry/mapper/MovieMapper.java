package com.outsera.goldenraspberry.mapper;

import com.outsera.goldenraspberry.controller.domain.response.MovieResponse;
import com.outsera.goldenraspberry.entity.Movie;
import com.outsera.goldenraspberry.entity.Producer;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;
import java.util.Set;

@Mapper(componentModel = "spring")
public interface MovieMapper {

    @Mapping(target = "studios", source = "studios", qualifiedByName = "sortStrings")
    @Mapping(target = "producers", source = "producers", qualifiedByName = "producersToSortedNames")
    MovieResponse toResponse(Movie movie);

    @Named("sortStrings")
    default List<String> sortStrings(Set<String> values) {
        return values == null ? List.of() : values.stream().sorted().toList();
    }

    @Named("producersToSortedNames")
    default List<String> producersToSortedNames(Set<Producer> producers) {
        return producers == null
                ? List.of()
                : producers.stream().map(Producer::getName).sorted().toList();
    }
}
