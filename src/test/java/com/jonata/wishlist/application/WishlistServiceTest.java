package com.jonata.wishlist.application;

import com.jonata.wishlist.domain.entity.Wishlist;
import com.jonata.wishlist.domain.exception.BusinessException;
import com.jonata.wishlist.domain.exception.NotFoundException;
import com.jonata.wishlist.domain.exception.WishlistLimitExceededException;
import com.jonata.wishlist.domain.repository.WishlistRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class WishlistServiceTest {

    @Mock
    private WishlistRepository wishlistRepository;

    private WishlistService wishlistService;

    @BeforeEach
    void setUp() {
        wishlistService = new WishlistService(wishlistRepository);
        ReflectionTestUtils.setField(wishlistService, "maxWishlistSize", 2);
    }

    @Test
    void addProduct_shouldAddProductSuccessfully() {
        String customerId = "c1";
        Wishlist.Product product = Wishlist.Product.builder().productId("p1").build();

        when(wishlistRepository.findByCustomerId(customerId)).thenReturn(Optional.empty());
        when(wishlistRepository.save(any(Wishlist.class))).thenAnswer(i -> i.getArgument(0));

        Wishlist result = wishlistService.addProduct(customerId, product);

        assertTrue(result.containsProduct("p1"));
        verify(wishlistRepository).save(result);
    }

    @Test
    void addProduct_shouldThrowExceptionWhenProductExists() {
        String customerId = "c1";
        Wishlist.Product product = Wishlist.Product.builder().productId("p1").build();

        List<Wishlist.Product> products = new ArrayList<>();
        products.add(product);

        Wishlist existing = Wishlist.builder().customerId(customerId).products(products).build();

        when(wishlistRepository.findByCustomerId(customerId)).thenReturn(Optional.of(existing));

        assertThrows(BusinessException.class, () -> wishlistService.addProduct(customerId, product));
    }

    @Test
    void addProduct_shouldThrowExceptionWhenLimitExceeded() {
        String customerId = "c1";
        Wishlist.Product product1 = Wishlist.Product.builder().productId("p1").build();
        Wishlist.Product product2 = Wishlist.Product.builder().productId("p2").build();
        Wishlist.Product newProduct = Wishlist.Product.builder().productId("p3").build();

        List<Wishlist.Product> products = new ArrayList<>(List.of(product1, product2));
        Wishlist existing = Wishlist.builder().customerId(customerId).products(products).build();

        when(wishlistRepository.findByCustomerId(customerId)).thenReturn(Optional.of(existing));

        assertThrows(WishlistLimitExceededException.class, () -> wishlistService.addProduct(customerId, newProduct));
    }

    @Test
    void removeProduct_shouldRemoveSuccessfully() {
        String customerId = "c1";
        Wishlist.Product product = Wishlist.Product.builder().productId("p1").build();

        List<Wishlist.Product> products = new ArrayList<>();
        products.add(product);

        Wishlist wishlist = Wishlist.builder().customerId(customerId).products(products).build();

        when(wishlistRepository.findByCustomerId(customerId)).thenReturn(Optional.of(wishlist));

        wishlistService.removeProduct(customerId, "p1");

        verify(wishlistRepository).save(any(Wishlist.class));
        assertFalse(wishlist.containsProduct("p1"));
    }

    @Test
    void removeProduct_shouldThrowWhenNotFound() {
        when(wishlistRepository.findByCustomerId("c1")).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> wishlistService.removeProduct("c1", "p1"));
    }

    @Test
    void getWishlist_shouldReturnWishlist() {
        Wishlist wishlist = Wishlist.builder().customerId("c1").build();
        when(wishlistRepository.findByCustomerId("c1")).thenReturn(Optional.of(wishlist));

        assertEquals(wishlist, wishlistService.getWishlist("c1"));
    }

    @Test
    void isProductInWishlist_shouldReturnTrueWhenPresent() {
        Wishlist.Product product = Wishlist.Product.builder().productId("p1").build();
        List<Wishlist.Product> products = new ArrayList<>(List.of(product));
        Wishlist wishlist = Wishlist.builder().customerId("c1").products(products).build();

        when(wishlistRepository.findByCustomerId("c1")).thenReturn(Optional.of(wishlist));

        assertTrue(wishlistService.isProductInWishlist("c1", "p1"));
    }

    @Test
    void isProductInWishlist_shouldReturnFalseWhenAbsent() {
        when(wishlistRepository.findByCustomerId("c1")).thenReturn(Optional.empty());

        assertFalse(wishlistService.isProductInWishlist("c1", "p1"));
    }
}
