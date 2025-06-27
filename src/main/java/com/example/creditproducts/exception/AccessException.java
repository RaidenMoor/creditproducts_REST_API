package com.example.creditproducts.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class AccessException extends RuntimeException {
  public AccessException() {
    super("У вас нет прав доступа к этим данным.");}
}
