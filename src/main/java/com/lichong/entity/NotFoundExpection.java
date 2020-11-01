package com.lichong.entity;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class NotFoundExpection extends RuntimeException {
    public NotFoundExpection() {
    }

    public NotFoundExpection(String message) {
        super(message);
    }

    public NotFoundExpection(String message, Throwable cause) {
        super(message, cause);
    }
}
