package com.jonata.wishlist.domain.exception;

import org.springframework.http.HttpStatus;

public class WishlistLimitExceededException extends BusinessException {
    public WishlistLimitExceededException(Integer maxsize) {
        super("Wishlist has reached the maximum limit of " + maxsize + " products", HttpStatus.UNPROCESSABLE_ENTITY);
    }
}