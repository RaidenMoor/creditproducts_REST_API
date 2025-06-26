package com.example.creditproducts.dto.security;

import lombok.Data;

@Data
public class RegisterRequest {
    private String username;
    private String password; // Пароль будет зашифрован перед сохранением

    public String getUsername(){
        return  username;
    }

    public String getPassword(){
        return password;
    }
}