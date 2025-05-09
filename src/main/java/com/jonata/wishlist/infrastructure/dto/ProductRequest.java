package com.jonata.wishlist.infrastructure.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@Schema(description = "Objeto de requisição para adicionar um produto à wishlist")
public class ProductRequest {

    @NotBlank
    @Schema(
            description = "Identificador único do produto",
            example = "prod-123",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private String productId;

    @NotBlank
    @Schema(
            description = "Nome do produto",
            example = "Smartphone Premium",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private String name;

    @NotNull
    @Positive
    @Schema(
            description = "Preço do produto em reais",
            example = "1299.99",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private Double price;
}