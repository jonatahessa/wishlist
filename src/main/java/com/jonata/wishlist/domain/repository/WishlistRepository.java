package com.jonata.wishlist.domain.repository;

import com.jonata.wishlist.domain.entity.Wishlist;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.Optional;

public interface WishlistRepository extends MongoRepository<Wishlist, String> {

    Optional<Wishlist> findByCustomerId(String customerId);

    @Query(value = "{'customerId': ?0, 'products.productId': ?1}", exists = true)
    boolean existsProductInWishlist(String customerId, String productId);
}