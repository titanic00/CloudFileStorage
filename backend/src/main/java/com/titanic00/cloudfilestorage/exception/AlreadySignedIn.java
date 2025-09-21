package com.titanic00.cloudfilestorage.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.CONFLICT)
public class AlreadySignedIn extends RuntimeException {

    public AlreadySignedIn(String message) { super(message); }
}
