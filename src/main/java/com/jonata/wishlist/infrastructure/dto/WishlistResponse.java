package com.jonata.wishlist.infrastructure.dto;

import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
@Schema(description = "Objeto de resposta com a wishlist completa de um cliente")
public class WishlistResponse {

    @Schema(
            description = "Identificador único do cliente",
            example = "cli-456"
    )
    private String customerId;

    @ArraySchema(
            schema = @Schema(implementation = ProductResponse.class),
            arraySchema = @Schema(
                    description = "Lista de produtos na wishlist do cliente"
            )
    )
    private List<ProductResponse> products;
}