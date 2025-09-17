package com.titanic00.cloudfilestorage.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.UNAUTHORIZED)
public class IncorrectCredentials extends RuntimeException {
    public IncorrectCredentials(String message) {
        super(message);
    }
}
