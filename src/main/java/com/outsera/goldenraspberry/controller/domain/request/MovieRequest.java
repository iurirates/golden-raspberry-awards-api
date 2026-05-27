package com.outsera.goldenraspberry.controller.domain.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.With;

import java.util.Set;

@With
public record MovieRequest(
        @NotNull(message = "O ano é obrigatório")
        @Min(value = 1800, message = "Ano inválido")
        @Max(value = 2999, message = "Ano inválido")
        Integer year,

        @NotBlank(message = "O título é obrigatório")
        String title,

        Set<String> studios,

        Set<String> producers,

        boolean winner) {
}
