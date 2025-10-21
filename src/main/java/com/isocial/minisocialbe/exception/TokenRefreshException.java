package com.isocial.minisocialbe.exception;

public class TokenRefreshException extends RuntimeException {
    public TokenRefreshException(String message, Throwable cause) {
        super(message, cause);
    }

    public TokenRefreshException(String message) {
        super(message);
    }
}
