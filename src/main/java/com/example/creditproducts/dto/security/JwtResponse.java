package com.example.creditproducts.dto.security;

import lombok.Data;

@Data
public class JwtResponse {
    private String token; // JWT токен для последующих запросов

    public JwtResponse(String token) {
        this.token = token;
    }

    public String getToken(){
        return token;
    }
}