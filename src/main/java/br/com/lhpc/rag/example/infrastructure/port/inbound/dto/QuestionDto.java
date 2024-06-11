package br.com.lhpc.rag.example.infrastructure.port.inbound.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record QuestionDto(

        @NotBlank
        String subject,

        @NotNull
        Seniority seniority

) {
}
