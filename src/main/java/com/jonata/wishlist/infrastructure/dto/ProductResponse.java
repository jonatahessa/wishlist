package com.jonata.wishlist.infrastructure.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProductResponse {
    private String productId;
    private String name;
    private Double price;
}
