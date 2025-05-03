package com.jonata.wishlist.application;

import com.jonata.wishlist.domain.entity.Wishlist;
import com.jonata.wishlist.domain.repository.WishlistRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class WishlistServiceTest {

    @Mock
    private WishlistRepository wishlistRepository;

    @InjectMocks
    private WishlistService wishlistService;

    @Test
    void shouldAddProductToNewWishlist() {
        String customerId = "customer1";
        Wishlist.Product product = Wishlist.Product.builder()
                .productId("prod1")
                .build();

        when(wishlistRepository.findByCustomerId(customerId)).thenReturn(Optional.empty());
        when(wishlistRepository.save(any(Wishlist.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Wishlist result = wishlistService.addProduct(customerId, product);

        assertNotNull(result);
        assertEquals(customerId, result.getCustomerId());
        assertEquals(1, result.getProducts().size());
        assertEquals("prod1", result.getProducts().get(0).getProductId());
        verify(wishlistRepository).save(any(Wishlist.class));
    }

    @Test
    void shouldAddProductToExistingWishlist() {
        String customerId = "customer1";
        Wishlist.Product product = Wishlist.Product.builder()
                .productId("prod2")
                .build();

        Wishlist existingWishlist = Wishlist.builder()
                .customerId(customerId)
                .build();
        existingWishlist.addProduct(Wishlist.Product.builder()
                .productId("prod1")
                .build());

        when(wishlistRepository.findByCustomerId(customerId)).thenReturn(Optional.of(existingWishlist));
        when(wishlistRepository.save(any(Wishlist.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Wishlist result = wishlistService.addProduct(customerId, product);

        assertEquals(2, result.getProducts().size());
        verify(wishlistRepository).save(existingWishlist);
    }

    @Test
    void shouldRemoveProductFromWishlist() {
        String customerId = "customer1";
        String productId = "prod1";

        Wishlist wishlist = Wishlist.builder()
                .customerId(customerId)
                .build();
        wishlist.addProduct(Wishlist.Product.builder()
                .productId(productId)
                .build());

        when(wishlistRepository.findByCustomerId(customerId)).thenReturn(Optional.of(wishlist));

        wishlistService.removeProduct(customerId, productId);

        assertTrue(wishlist.getProducts().isEmpty());
        verify(wishlistRepository).save(wishlist);
    }

    @Test
    void shouldGetEmptyWishlistWhenNotFound() {
        String customerId = "customer1";

        when(wishlistRepository.findByCustomerId(customerId)).thenReturn(Optional.empty());

        Wishlist result = wishlistService.getWishlist(customerId);

        assertEquals(customerId, result.getCustomerId());
        assertTrue(result.getProducts().isEmpty());
    }

    @Test
    void shouldCheckProductPresenceInWishlist() {
        String customerId = "customer1";
        String productId = "prod1";

        when(wishlistRepository.existsProductInWishlist(customerId, productId)).thenReturn(true);

        boolean result = wishlistService.isProductInWishlist(customerId, productId);

        assertTrue(result);
        verify(wishlistRepository).existsProductInWishlist(customerId, productId);
    }
}