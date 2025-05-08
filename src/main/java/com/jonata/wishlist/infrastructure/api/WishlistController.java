package com.jonata.wishlist.infrastructure.api;

import com.jonata.wishlist.application.WishlistService;
import com.jonata.wishlist.domain.entity.Wishlist;
import com.jonata.wishlist.infrastructure.dto.ProductPresenceResponse;
import com.jonata.wishlist.infrastructure.dto.ProductRequest;
import com.jonata.wishlist.infrastructure.dto.ProductResponse;
import com.jonata.wishlist.infrastructure.dto.WishlistResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/wishlist")
@RequiredArgsConstructor
public class WishlistController {

    private final WishlistService wishlistService;

    @PostMapping("/{customerId}/products")
    public ResponseEntity<WishlistResponse> addProduct(
            @PathVariable String customerId,
            @RequestBody @Valid ProductRequest productRequest) {

        var product = Wishlist.Product.builder()
                .productId(productRequest.getProductId())
                .name(productRequest.getName())
                .price(productRequest.getPrice())
                .build();

        var wishlist = wishlistService.addProduct(customerId, product);
        return ResponseEntity.status(HttpStatus.CREATED).body(toResponse(wishlist));
    }

    @DeleteMapping("/{customerId}/products/{productId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeProduct(
            @PathVariable String customerId,
            @PathVariable String productId) {
        wishlistService.removeProduct(customerId, productId);
    }

    @GetMapping("/{customerId}")
    public ResponseEntity<WishlistResponse> getWishlist(@PathVariable String customerId) {
        return ResponseEntity.ok(toResponse(wishlistService.getWishlist(customerId)));
    }

    @GetMapping("/{customerId}/products/{productId}")
    public ResponseEntity<ProductPresenceResponse> checkProduct(
            @PathVariable String customerId,
            @PathVariable String productId) {
        return ResponseEntity.ok(
                ProductPresenceResponse.builder()
                        .present(wishlistService.isProductInWishlist(customerId, productId))
                        .build()
        );
    }

    private WishlistResponse toResponse(Wishlist wishlist) {
        return WishlistResponse.builder()
                .customerId(wishlist.getCustomerId())
                .products(wishlist.getProducts().stream()
                        .map(p -> ProductResponse.builder()
                                .productId(p.getProductId())
                                .name(p.getName())
                                .price(p.getPrice())
                                .build())
                        .collect(Collectors.toList()))
                .build();
    }
}