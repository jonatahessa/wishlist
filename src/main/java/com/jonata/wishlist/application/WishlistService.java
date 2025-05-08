package com.jonata.wishlist.application;

import com.jonata.wishlist.domain.entity.Wishlist;
import com.jonata.wishlist.domain.exception.BusinessException;
import com.jonata.wishlist.domain.exception.NotFoundException;
import com.jonata.wishlist.domain.exception.WishlistLimitExceededException;
import com.jonata.wishlist.domain.repository.WishlistRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class WishlistService {

    private final WishlistRepository wishlistRepository;

    @Value("${wishlist.size:20}")
    private int maxWishlistSize;

    @Transactional
    public Wishlist addProduct(String customerId, Wishlist.Product product) {
        Wishlist wishlist = wishlistRepository.findByCustomerId(customerId)
                .orElseGet(() -> Wishlist.builder()
                        .customerId(customerId)
                        .build());

        if (wishlist.getProducts().size() >= maxWishlistSize) {
            throw new WishlistLimitExceededException(maxWishlistSize);
        }

        try {
            wishlist.addProduct(product);
            return wishlistRepository.save(wishlist);
        } catch (IllegalArgumentException e) {
            throw new BusinessException(e.getMessage());
        }
    }

    @Transactional
    public void removeProduct(String customerId, String productId) {
        Wishlist wishlist = wishlistRepository.findByCustomerId(customerId)
                .orElseThrow(() -> new NotFoundException("Wishlist not found"));

        if (!wishlist.containsProduct(productId)) {
            throw new NotFoundException("Product not found in wishlist");
        }

        wishlist.removeProduct(productId);
        wishlistRepository.save(wishlist);
    }

    public Wishlist getWishlist(String customerId) {
        return wishlistRepository.findByCustomerId(customerId)
                .orElseThrow(() -> new NotFoundException("Wishlist not found"));
    }

    public boolean isProductInWishlist(String customerId, String productId) {
        return wishlistRepository.findByCustomerId(customerId)
                .map(w -> w.containsProduct(productId))
                .orElse(false);
    }
}
