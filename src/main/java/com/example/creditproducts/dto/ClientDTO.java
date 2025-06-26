package com.example.creditproducts.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;


@NoArgsConstructor
@AllArgsConstructor
public class ClientDTO extends GenericDTO{
    @Schema(description = "имя")
    private String firstName;

    @Schema(description = "фамилия")
    private String lastName;

    @Schema(description = "электронная почта")
    @Email
    private String email;

    @Schema(description = "телефон")
    @Pattern(regexp = "^(\\+7|8)[\\s\\-]?\\(?\\d{3}\\)?[\\s\\-]?\\d{3}[\\s\\-]?\\d{2}[\\s\\-]?\\d{2}$",
            message = "Неверный формат номера телефона")
    private String phone;

    @Schema(description = "номер паспорта")
    @Size(min = 6, max = 6)
    @Pattern(regexp = "^\\d{6}$", message = "Номер паспорта должен состоять из 6 цифр")
    private String passportNumber;

    @Schema(description = "дата регистрации")
    private LocalDate registrationDate;

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

    public LocalDate getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(LocalDate regictrationDate) {
        this.registrationDate = regictrationDate;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}
