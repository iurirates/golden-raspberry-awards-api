package com.outsera.goldenraspberry.controller.api;

import com.outsera.goldenraspberry.controller.domain.request.MovieRequest;
import com.outsera.goldenraspberry.controller.domain.response.MovieResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@Tag(name = "Filmes", description = "CRUD de filmes indicados/vencedores")
@RequestMapping("/api/movies")
public interface MovieApi {

    @Operation(summary = "Lista todos os filmes")
    @ApiResponses(@ApiResponse(responseCode = "200", description = "Lista retornada com sucesso"))
    @GetMapping
    ResponseEntity<List<MovieResponse>> findAll();

    @Operation(summary = "Busca um filme por id")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Filme encontrado"),
            @ApiResponse(responseCode = "404", description = "Filme não encontrado")
    })
    @GetMapping("/{id}")
    ResponseEntity<MovieResponse> findById(@PathVariable Long id);

    @Operation(summary = "Cria um novo filme")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Filme criado"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos")
    })
    @PostMapping
    ResponseEntity<MovieResponse> create(@Valid @RequestBody MovieRequest request,
                                         UriComponentsBuilder uriBuilder);

    @Operation(summary = "Atualiza um filme existente")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Filme atualizado"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos"),
            @ApiResponse(responseCode = "404", description = "Filme não encontrado")
    })
    @PutMapping("/{id}")
    ResponseEntity<MovieResponse> update(@PathVariable Long id,
                                         @Valid @RequestBody MovieRequest request);

    @Operation(summary = "Remove um filme")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Filme removido"),
            @ApiResponse(responseCode = "404", description = "Filme não encontrado")
    })
    @DeleteMapping("/{id}")
    ResponseEntity<Void> delete(@PathVariable Long id);
}
