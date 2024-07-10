package com.homedelivery.service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.METHOD_NOT_ALLOWED)
public class DeleteObjectException extends RuntimeException {

    public DeleteObjectException(String message) {
        super(message);
    }

}