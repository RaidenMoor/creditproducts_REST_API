package com.example.creditproducts.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.math.BigDecimal;

@ResponseStatus(HttpStatus.CONFLICT)
public class DublicateException extends RuntimeException {
    public DublicateException(String message) {
        super(message);
    }
}
