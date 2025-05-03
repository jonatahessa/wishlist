package com.jonata.wishlist.application;

import com.jonata.wishlist.domain.entity.Wishlist;
import com.jonata.wishlist.domain.repository.WishlistRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class WishlistService {

    private final WishlistRepository wishlistRepository;

    @Transactional
    public Wishlist addProduct(String customerId, Wishlist.Product product) {
        return wishlistRepository.findByCustomerId(customerId)
                .map(wishlist -> {
                    wishlist.addProduct(product);
                    return wishlistRepository.save(wishlist);
                })
                .orElseGet(() -> wishlistRepository.save(
                        Wishlist.builder()
                                .customerId(customerId)
                                .products(List.of(product))
                                .build()
                ));
    }

    @Transactional
    public void removeProduct(String customerId, String productId) {
        wishlistRepository.findByCustomerId(customerId)
                .ifPresent(wishlist -> {
                    wishlist.removeProduct(productId);
                    wishlistRepository.save(wishlist);
                });
    }

    public Wishlist getWishlist(String customerId) {
        return wishlistRepository.findByCustomerId(customerId)
                .orElse(Wishlist.builder().customerId(customerId).build());
    }

    public boolean isProductInWishlist(String customerId, String productId) {
        return wishlistRepository.existsProductInWishlist(customerId, productId);
    }
}