package com.jonata.wishlist.infrastructure.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@Schema(description = "Objeto de resposta com informações de um produto na wishlist")
public class ProductResponse {

    @Schema(
            description = "Identificador único do produto",
            example = "prod-123"
    )
    private String productId;

    @Schema(
            description = "Nome do produto",
            example = "Smartphone Premium"
    )
    private String name;

    @Schema(
            description = "Preço do produto em reais",
            example = "1299.99"
    )
    private Double price;
}