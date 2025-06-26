package com.example.creditproducts.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.math.BigDecimal;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InvalidAmountException extends RuntimeException {
    public InvalidAmountException(BigDecimal min, BigDecimal max) {
        super("Сумма должна не превышать " + max + " и быть больше " + min);}
}
