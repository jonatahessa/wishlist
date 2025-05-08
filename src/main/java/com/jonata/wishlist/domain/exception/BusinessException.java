package com.jonata.wishlist.domain.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class BusinessException extends ApiException {
    public BusinessException(String message, HttpStatus status) {
        super(message, status);
    }

    public BusinessException(String message) {
        this(message, HttpStatus.BAD_REQUEST);
    }
}