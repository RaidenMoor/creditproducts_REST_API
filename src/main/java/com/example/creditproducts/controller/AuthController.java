package com.example.creditproducts.controller;

import com.example.creditproducts.dto.UserDTO;
import com.example.creditproducts.dto.security.JwtResponse;
import com.example.creditproducts.dto.security.LoginRequest;
import com.example.creditproducts.dto.security.RegisterRequest;
import com.example.creditproducts.jwt.JwtUtils;
import com.example.creditproducts.repository.RoleRepository;
import com.example.creditproducts.repository.UserRepository;
import com.example.creditproducts.security.CustomUserDetails;
import com.example.creditproducts.service.SecurityService;
import com.example.creditproducts.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {


    private final SecurityService securityService;
    private final UserService userService;
    @Autowired
    public AuthController(UserService userService,
                          SecurityService securityService){
        this.userService = userService;
        this.securityService = securityService;
    }

    @PostMapping("/login")
    @Operation(summary = "Аутентификация")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Аутентификация прошла успешно"),
            @ApiResponse(responseCode = "401", description = "Неверные учетные данные пользователя"),
            @ApiResponse(responseCode = "404", description = "Пользователь не найден")
    })
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {

        // Возвращаем токен
        return ResponseEntity.ok(new JwtResponse(securityService.loginUser(request)));
    }

    @PostMapping("/register")
    @Operation(summary = "Регистрация")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Рестрация прошла успешно"),
    })
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {

        return securityService.createNewUser(request);
    }
}
