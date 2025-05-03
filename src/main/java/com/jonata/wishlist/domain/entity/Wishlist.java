package com.jonata.wishlist.domain.entity;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@Document(collection = "wishlist")
@CompoundIndex(def = "{'customerId': 1, 'products.productId': 1}", unique = true)
public class Wishlist {

    @Id
    private String id;
    private String customerId;

    @Builder.Default
    private List<Product> products = new ArrayList<>();

    public boolean isFull() {
        return products.size() >= 20;
    }

    public boolean containsProduct(String productId) {
        return products.stream().anyMatch(p -> p.getProductId().equals(productId));
    }

    public void addProduct(Product product) {
        if (isFull()) {
            throw new IllegalStateException("Wishlist has reached the maximum limit of 20 products");
        }
        if (containsProduct(product.getProductId())) {
            throw new IllegalArgumentException("Product already exists in the wishlist");
        }
        products.add(product);
    }

    public void removeProduct(String productId) {
        products.removeIf(p -> p.getProductId().equals(productId));
    }

    @Data
    @Builder
    public static class Product {
        private String productId;
        private String name;
        private Double price;
    }
}