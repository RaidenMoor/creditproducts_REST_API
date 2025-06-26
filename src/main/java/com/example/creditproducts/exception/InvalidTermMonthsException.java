package com.example.creditproducts.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.math.BigDecimal;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InvalidTermMonthsException extends RuntimeException {
    public InvalidTermMonthsException(int min, int max) {
        super("Срок погашения кредита не должен превышать " + max + " месяцев" +
                " и быть дольше " + min + " месяцев");}
}
