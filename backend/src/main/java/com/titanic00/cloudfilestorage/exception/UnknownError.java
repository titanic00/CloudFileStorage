package com.titanic00.cloudfilestorage.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
public class UnknownError extends RuntimeException {
    public UnknownError(String message) {
        super(message);
    }
}
