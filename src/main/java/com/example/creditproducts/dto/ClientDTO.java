package com.example.creditproducts.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@NoArgsConstructor
@AllArgsConstructor
public class ClientDTO extends GenericDTO{
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

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPassportNumber() {
        return passportNumber;
    }

    public void setPassportNumber(String passportNumber) {
        this.passportNumber = passportNumber;
    }

    public String getRegictrationDate() {
        return regictrationDate;
    }

    public void setRegictrationDate(String regictrationDate) {
        this.regictrationDate = regictrationDate;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}
