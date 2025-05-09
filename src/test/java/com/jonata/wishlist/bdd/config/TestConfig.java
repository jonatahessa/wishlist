package com.jonata.wishlist.bdd.config;

import com.jonata.wishlist.application.WishlistService;
import com.jonata.wishlist.domain.repository.WishlistRepository;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

import static org.mockito.Mockito.mock;

@TestConfiguration
public class TestConfig {

    @Bean
    @Primary
    public WishlistService mockWishlistService() {
        return new WishlistService(mock(WishlistRepository.class));
    }

    @Bean
    public WishlistService realWishlistService(WishlistRepository wishlistRepository) {
        return new WishlistService(wishlistRepository);
    }

    @Bean
    @Primary
    public WishlistRepository mockWishlistRepository() {
        return mock(WishlistRepository.class);
    }
}