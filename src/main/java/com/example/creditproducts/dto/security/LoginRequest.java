package com.example.creditproducts.dto.security;


public class LoginRequest {
    private String username; // Логин пользователя
    private String password; // Пароль в открытом виде (передается при аутентификации)

    public String getUsername(){
        return  username;
    }

    public String getPassword(){
        return password;
    }

    public void setUsername(String username){
        this.username = username;
    }

    public void setPassword(String password){
        this.password = password;
    }
}