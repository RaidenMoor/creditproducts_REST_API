package com.example.creditproducts.controller;

import com.example.creditproducts.dto.UserDTO;
import com.example.creditproducts.dto.security.JwtResponse;
import com.example.creditproducts.dto.security.LoginRequest;
import com.example.creditproducts.dto.security.RegisterRequest;
import com.example.creditproducts.jwt.JwtUtils;
import com.example.creditproducts.repository.RoleRepository;
import com.example.creditproducts.repository.UserRepository;
import com.example.creditproducts.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    UserService userService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        // 1. Создаём объект аутентификации
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()));
        // 2. Сохраняем аутентификацию в контексте
        SecurityContextHolder.getContext().setAuthentication(authentication);
        // 3. Генерируем токен
        String jwt = jwtUtils.generateToken((UserDetails) authentication.getPrincipal());
        // Возвращаем токен
        return ResponseEntity.ok(new JwtResponse(jwt));
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        // Проверка на существование пользователя
        if (userRepository.existsByUsername(request.getUsername())) {
            return ResponseEntity.badRequest().body("Error: Username is already taken!");
        }
        UserDTO userDTO = new UserDTO();
        userDTO.setPassword(passwordEncoder.encode(request.getPassword()));
        userDTO.setUsername(request.getUsername());

        userService.create(userDTO);

        return ResponseEntity.ok("User registered successfully!");
    }

}
