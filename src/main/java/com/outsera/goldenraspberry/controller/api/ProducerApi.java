package com.outsera.goldenraspberry.controller.api;

import com.outsera.goldenraspberry.controller.domain.response.AwardIntervalsResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Tag(name = "Produtores", description = "Consultas sobre prêmios de produtores")
@RequestMapping("/api/producers")
public interface ProducerApi {

    @Operation(
            summary = "Intervalos de prêmios",
            description = "Retorna o produtor com maior intervalo entre dois prêmios consecutivos "
                    + "e o que obteve dois prêmios mais rapidamente.")
    @ApiResponses(@ApiResponse(responseCode = "200", description = "Intervalos calculados com sucesso"))
    @GetMapping("/award-intervals")
    ResponseEntity<AwardIntervalsResponse> getAwardIntervals();
}
