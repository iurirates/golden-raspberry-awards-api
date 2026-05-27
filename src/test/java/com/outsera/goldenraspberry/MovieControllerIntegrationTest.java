package com.outsera.goldenraspberry;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.outsera.goldenraspberry.controller.domain.request.MovieRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class MovieControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldLoadAllMoviesFromCsvOnStartup() throws Exception {
        mockMvc.perform(get("/api/movies"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(206));
    }

    @Test
    void shouldCreateMovieAndReturnLocation() throws Exception {
        MovieRequest request = new MovieRequest(
                2030, "Filme de Teste", Set.of("Estúdio X"), Set.of("Produtor Teste"), false);

        mockMvc.perform(post("/api/movies")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.title").value("Filme de Teste"))
                .andExpect(jsonPath("$.year").value(2030))
                .andExpect(jsonPath("$.producers[0]").value("Produtor Teste"));
    }

    @Test
    void shouldFindMovieById() throws Exception {
        mockMvc.perform(get("/api/movies/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void shouldUpdateMovie() throws Exception {
        MovieRequest request = new MovieRequest(
                1999, "Título Atualizado", Set.of("Novo Estúdio"), Set.of("Novo Produtor"), true);

        mockMvc.perform(put("/api/movies/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Título Atualizado"))
                .andExpect(jsonPath("$.winner").value(true));
    }

    @Test
    void shouldDeleteMovie() throws Exception {
        mockMvc.perform(delete("/api/movies/1"))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/api/movies/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldReturnNotFoundForUnknownMovie() throws Exception {
        mockMvc.perform(get("/api/movies/999999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404));
    }

    @Test
    void shouldReturnBadRequestForInvalidPayload() throws Exception {
        String invalidJson = "{\"year\": null, \"title\": \"\"}";

        mockMvc.perform(post("/api/movies")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400));
    }
}
