package com.jonata.wishlist.infrastructure.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@Schema(description = "Objeto de resposta para verificação de presença de produto na wishlist")
public class ProductPresenceResponse {

    @Schema(
            description = "Indica se o produto está presente na wishlist do cliente",
            example = "true"
    )
    private boolean present;
}