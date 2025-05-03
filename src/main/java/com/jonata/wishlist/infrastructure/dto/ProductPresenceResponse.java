package com.jonata.wishlist.infrastructure.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProductPresenceResponse {
    private boolean present;
}
