package com.jonata.wishlist.domain.entity;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class WishlistTest {

    @Test
    void shouldAddProductToWishlist() {
        Wishlist wishlist = Wishlist.builder()
                .customerId("customer1")
                .build();

        Wishlist.Product product = Wishlist.Product.builder()
                .productId("prod1")
                .name("Product 1")
                .price(100.0)
                .build();

        wishlist.addProduct(product);

        assertFalse(wishlist.getProducts().isEmpty());
        assertEquals(1, wishlist.getProducts().size());
        assertEquals("prod1", wishlist.getProducts().get(0).getProductId());
    }

    @Test
    void shouldThrowWhenAddingDuplicateProduct() {
        Wishlist wishlist = Wishlist.builder()
                .customerId("customer1")
                .build();

        Wishlist.Product product = Wishlist.Product.builder()
                .productId("prod1")
                .build();

        wishlist.addProduct(product);

        assertThrows(IllegalArgumentException.class, () -> wishlist.addProduct(product));
    }

    @Test
    void shouldThrowWhenExceedingMaxProducts() {
        Wishlist wishlist = Wishlist.builder()
                .customerId("customer1")
                .build();

        for (int i = 1; i <= 20; i++) {
            wishlist.addProduct(Wishlist.Product.builder()
                    .productId("prod" + i)
                    .build());
        }

        assertTrue(wishlist.isFull());
        assertThrows(IllegalStateException.class, () ->
                wishlist.addProduct(Wishlist.Product.builder()
                        .productId("prod21")
                        .build()));
    }

    @Test
    void shouldRemoveProductFromWishlist() {
        Wishlist wishlist = Wishlist.builder()
                .customerId("customer1")
                .build();

        Wishlist.Product product = Wishlist.Product.builder()
                .productId("prod1")
                .build();

        wishlist.addProduct(product);
        wishlist.removeProduct("prod1");

        assertTrue(wishlist.getProducts().isEmpty());
    }

    @Test
    void shouldCheckProductPresence() {
        Wishlist wishlist = Wishlist.builder()
                .customerId("customer1")
                .build();

        wishlist.addProduct(Wishlist.Product.builder()
                .productId("prod1")
                .build());

        assertTrue(wishlist.containsProduct("prod1"));
        assertFalse(wishlist.containsProduct("prod2"));
    }
}