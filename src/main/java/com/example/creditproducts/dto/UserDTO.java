package com.example.creditproducts.dto;

import com.example.creditproducts.model.Role;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;


@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO extends GenericDTO{
    @Schema(description = "Логин")
    private String username;

    @Schema(description = "Пароль")
    private String password;

    @Schema(description = "Роль")
    private Role role;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }
}