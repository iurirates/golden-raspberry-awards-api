package com.outsera.goldenraspberry.parser.impl;

import com.outsera.goldenraspberry.parser.ProducerNameParser;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Component
public class DelimitedProducerNameParser implements ProducerNameParser {
    private static final Pattern SEPARATOR = Pattern.compile("\\s*,\\s*and\\s+|\\s*,\\s*|\\s+and\\s+");

    @Override
    public Set<String> parse(String rawProducers) {
        if (rawProducers == null || rawProducers.isBlank()) {
            return new LinkedHashSet<>();
        }
        return Arrays.stream(SEPARATOR.split(rawProducers))
                .map(String::trim)
                .filter(name -> !name.isEmpty())
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }
}
