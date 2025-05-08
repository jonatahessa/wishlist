package com.jonata.wishlist.domain.entity;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class WishlistTest {

    @Test
    void shouldAddProductSuccessfully() {
        Wishlist wishlist = Wishlist.builder().customerId("customer1").build();
        Wishlist.Product product = Wishlist.Product.builder().productId("prod1").build();

        wishlist.addProduct(product);

        assertEquals(1, wishlist.getProducts().size());
        assertEquals("prod1", wishlist.getProducts().get(0).getProductId());
    }

    @Test
    void shouldThrowWhenProductExists() {
        String customerId = "1";
        Wishlist wishlist = Wishlist.builder()
                .customerId(customerId)
                .build();
        Wishlist.Product product = Wishlist.Product.builder().productId("prod1").build();
        wishlist.getProducts().add(product);

        assertThrows(IllegalArgumentException.class, () -> {
            wishlist.addProduct(product);
        });
    }

    @Test
    void shouldRemoveProductSuccessfully() {
        Wishlist wishlist = Wishlist.builder().customerId("customer1").build();
        Wishlist.Product product = Wishlist.Product.builder().productId("prod1").build();
        wishlist.getProducts().add(product);

        wishlist.removeProduct("prod1");

        assertTrue(wishlist.getProducts().isEmpty());
    }

    @Test
    void shouldCheckProductPresence() {
        Wishlist wishlist = Wishlist.builder().customerId("customer1").build();
        wishlist.getProducts().add(Wishlist.Product.builder().productId("prod1").build());

        assertTrue(wishlist.containsProduct("prod1"));
        assertFalse(wishlist.containsProduct("prod2"));
    }
}