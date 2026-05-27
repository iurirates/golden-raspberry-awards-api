package com.outsera.goldenraspberry.loader;

import com.outsera.goldenraspberry.exception.CsvParsingException;
import com.outsera.goldenraspberry.parser.MovieParser;
import com.outsera.goldenraspberry.parser.domain.ParsedMovie;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class MovieDataLoader implements ApplicationRunner {

    private final ResourceLoader resourceLoader;
    private final MovieParser movieParser;
    private final MovieImporter movieImporter;

    @Value("${application.csv.movies-file}")
    private String csvLocation;

    @Override
    public void run(ApplicationArguments args) {
        Resource resource = resourceLoader.getResource(csvLocation);
        if (!resource.exists()) {
            throw new CsvParsingException("Arquivo CSV não encontrado em: " + csvLocation);
        }

        try (InputStream inputStream = resource.getInputStream()) {
            List<ParsedMovie> movies = movieParser.parse(inputStream);
            int imported = movieImporter.importMovies(movies);
            log.info("Importação concluída: {} filmes carregados a partir de '{}'.", imported, csvLocation);
        } catch (IOException e) {
            throw new CsvParsingException("Falha ao abrir o arquivo CSV: " + csvLocation, e);
        }
    }
}
