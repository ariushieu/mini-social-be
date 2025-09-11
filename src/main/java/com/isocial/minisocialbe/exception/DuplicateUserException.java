package com.isocial.minisocialbe.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class DuplicateUserException extends RuntimeException{
    public DuplicateUserException(String message) {
        super(message);
    }
}
