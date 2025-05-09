package com.jonata.wishlist.infrastructure.api;

import com.jonata.wishlist.application.WishlistService;
import com.jonata.wishlist.domain.entity.Wishlist;
import com.jonata.wishlist.infrastructure.dto.ProductPresenceResponse;
import com.jonata.wishlist.infrastructure.dto.ProductRequest;
import com.jonata.wishlist.infrastructure.dto.WishlistResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class WishlistControllerTest {

    @Mock
    private WishlistService wishlistService;

    @InjectMocks
    private WishlistController wishlistController;

    @Test
    void shouldAddProductAndReturnCreated() {
        ProductRequest request = ProductRequest.builder().productId("prod1").name("Product 1").price(100.0).build();
        Wishlist wishlist = Wishlist.builder()
                .customerId("customer1")
                .build();
        wishlist.addProduct(Wishlist.Product.builder()
                .productId("prod1")
                .name("Product 1")
                .price(100.0)
                .build());

        when(wishlistService.addProduct(anyString(), any(Wishlist.Product.class))).thenReturn(wishlist);

        ResponseEntity<WishlistResponse> response = wishlistController.addProduct("customer1", request);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("customer1", response.getBody().getCustomerId());
        assertEquals(1, response.getBody().getProducts().size());
    }

    @Test
    void shouldRemoveProductAndReturnNoContent() {
        wishlistController.removeProduct("customer1", "prod1");

        assertDoesNotThrow(() -> wishlistController.removeProduct("customer1", "prod1"));
    }

    @Test
    void shouldGetWishlist() {
        Wishlist wishlist = Wishlist.builder()
                .customerId("customer1")
                .build();
        wishlist.addProduct(Wishlist.Product.builder()
                .productId("prod1")
                .name("Product 1")
                .price(100.0)
                .build());

        when(wishlistService.getWishlist("customer1")).thenReturn(wishlist);

        ResponseEntity<WishlistResponse> response = wishlistController.getWishlist("customer1");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("customer1", response.getBody().getCustomerId());
        assertEquals(1, response.getBody().getProducts().size());
    }

    @Test
    void shouldCheckProductPresence() {
        when(wishlistService.isProductInWishlist("customer1", "prod1")).thenReturn(true);

        ResponseEntity<ProductPresenceResponse> response =
                wishlistController.checkProduct("customer1", "prod1");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().isPresent());
    }

    @Test
    void shouldReturnEmptyWishlistWhenNotFound() {
        when(wishlistService.getWishlist("customer1")).thenReturn(
                Wishlist.builder().customerId("customer1").build());

        ResponseEntity<WishlistResponse> response = wishlistController.getWishlist("customer1");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("customer1", response.getBody().getCustomerId());
        assertTrue(response.getBody().getProducts().isEmpty());
    }
}