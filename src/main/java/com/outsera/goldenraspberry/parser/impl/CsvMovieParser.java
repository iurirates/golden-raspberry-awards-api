package com.outsera.goldenraspberry.parser.impl;

import com.outsera.goldenraspberry.exception.CsvParsingException;
import com.outsera.goldenraspberry.parser.MovieParser;
import com.outsera.goldenraspberry.parser.ProducerNameParser;
import com.outsera.goldenraspberry.parser.domain.ParsedMovie;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class CsvMovieParser implements MovieParser {

    private static final String DELIMITER = ";";
    private static final String WINNER_FLAG = "yes";
    private static final int EXPECTED_COLUMNS = 5;

    private final ProducerNameParser producerNameParser;

    @Override
    public List<ParsedMovie> parse(InputStream inputStream) {
        List<ParsedMovie> movies = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
            String header = reader.readLine();
            if (header == null) {
                throw new CsvParsingException("Arquivo CSV vazio.");
            }

            String line;
            int lineNumber = 1;
            while ((line = reader.readLine()) != null) {
                lineNumber++;
                if (line.isBlank()) {
                    continue;
                }
                movies.add(toParsedMovie(line, lineNumber));
            }
        } catch (IOException e) {
            throw new CsvParsingException("Falha ao ler o arquivo CSV de filmes.", e);
        }
        return movies;
    }

    private ParsedMovie toParsedMovie(String line, int lineNumber) {
        String[] columns = line.split(DELIMITER, -1);
        if (columns.length < EXPECTED_COLUMNS - 1) {
            throw new CsvParsingException(
                    "Linha %d com número de colunas inválido: '%s'".formatted(lineNumber, line));
        }

        int year = parseYear(columns[0], lineNumber);
        String title = columns[1].trim();
        Set<String> studios = splitStudios(columns[2]);
        Set<String> producers = producerNameParser.parse(columns[3]);
        boolean winner = columns.length >= EXPECTED_COLUMNS
                && WINNER_FLAG.equalsIgnoreCase(columns[4].trim());

        return new ParsedMovie(year, title, studios, producers, winner);
    }

    private int parseYear(String raw, int lineNumber) {
        try {
            return Integer.parseInt(raw.trim());
        } catch (NumberFormatException e) {
            throw new CsvParsingException(
                    "Ano inválido na linha %d: '%s'".formatted(lineNumber, raw), e);
        }
    }

    private Set<String> splitStudios(String raw) {
        if (raw == null || raw.isBlank()) {
            return new LinkedHashSet<>();
        }
        return Arrays.stream(raw.split(","))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }
}
