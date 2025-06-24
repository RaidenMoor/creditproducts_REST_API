package com.example.restbank.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ClientDTO extends GenericDTO {
    @Schema(description = "имя")
    private String firstName;

    @Schema(description = "фамилия")
    private String lastName;

    @Schema(description = "электронная почта")
    private String email;

    @Schema(description = "телефон")
    private String phone;

    @Schema(description = "номер паспорта")
    private String passportNumber;

    @Schema(description = "дата регистрации")
    private String regictrationDate;

    @Schema(description = "идентификатор пользователя")
    private Long userId;
}
