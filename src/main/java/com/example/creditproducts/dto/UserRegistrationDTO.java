package com.example.restbank.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class UserRegistrationDTO {
    @Schema(description = "Логин", required = true)
    private String username;

    @Schema(description = "Пароль", required = true)
    private String password;

    @Schema(description = "Имя", required = true)
    private String firstName;

    @Schema(description = "Фамилия", required = true)
    private String lastName;

    @Schema(description = "Email", required = true)
    private String email;

    @Schema(description = "Телефон")
    private String phone;

    @Schema(description = "Паспортные данные")
    private String passportNumber;
}
