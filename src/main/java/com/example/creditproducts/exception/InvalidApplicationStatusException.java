package com.example.creditproducts.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InvalidApplicationStatusException extends RuntimeException {
    public InvalidApplicationStatusException(String invalidStatus) {
        super("Введен некорректный статус.");}
}
