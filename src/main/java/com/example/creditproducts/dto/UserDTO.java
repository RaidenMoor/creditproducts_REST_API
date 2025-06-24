package com.example.restbank.dto;

import com.example.restbank.model.Role;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Setter
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
}
