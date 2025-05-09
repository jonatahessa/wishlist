package com.jonata.wishlist.infrastructure.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
@Schema(description = "Resposta padrão para erros da API")
public class ErrorResponse {

    @Schema(
            description = "Timestamp do erro",
            example = "2023-12-01T10:15:30"
    )
    private LocalDateTime timestamp;

    @Schema(
            description = "Código HTTP do erro",
            example = "404"
    )
    private int status;

    @Schema(
            description = "Tipo do erro",
            example = "NOT_FOUND"
    )
    private String error;

    @Schema(
            description = "Mensagem descritiva do erro",
            example = "Wishlist não encontrada"
    )
    private String message;

    @Schema(
            description = "Caminho da requisição que causou o erro",
            example = "/api/wishlist/12345"
    )
    private String path;
}