package com.example.creditproducts.controller;

import com.example.creditproducts.dto.UserDTO;
import com.example.creditproducts.dto.security.JwtResponse;
import com.example.creditproducts.dto.security.LoginRequest;
import com.example.creditproducts.dto.security.RegisterRequest;
import com.example.creditproducts.jwt.JwtUtils;
import com.example.creditproducts.repository.RoleRepository;
import com.example.creditproducts.repository.UserRepository;
import com.example.creditproducts.security.CustomUserDetails;
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


    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtils jwtUtils;


    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;


    private PasswordEncoder passwordEncoder;


    UserService userService;
    public AuthController(UserService userService){
        this.userService = userService;
    }

    @PostMapping("/login")
    @Operation(summary = "Аутентификация")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Аутентификация прошла успешно"),
            @ApiResponse(responseCode = "401", description = "Неверные учетные данные пользователя"),
            @ApiResponse(responseCode = "404", description = "Пользователь не найден")
    })
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {

        if(!userRepository.existsByUsername(request.getUsername()))
            throw new UsernameNotFoundException(request.getUsername());

        // 1. Создаём объект аутентификации
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()));

        // 2. Сохраняем аутентификацию в контексте
        SecurityContextHolder.getContext().setAuthentication(authentication);
        // 3. Генерируем токен
        String jwt = jwtUtils.generateToken((CustomUserDetails) authentication.getPrincipal());

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        // Возвращаем токен
        return ResponseEntity.ok(new JwtResponse(jwt));
    }

    @PostMapping("/register")
    @Operation(summary = "Регистрация")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Рестрация прошла успешно"),
    })
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        // Проверка на существование пользователя
        if (userRepository.existsByUsername(request.getUsername())) {
            return ResponseEntity.badRequest().body("Error: Username is already taken!");
        }
        UserDTO userDTO = new UserDTO();
        userDTO.setPassword(passwordEncoder.encode(request.getPassword()));
        userDTO.setUsername(request.getUsername());

        userService.create(userDTO);

        return ResponseEntity.ok("Пользователь успешно зарегистрирован!");
    }

    @Autowired
    public void setAuthenticationManager(AuthenticationManager authenticationManager){
        this.authenticationManager = authenticationManager;
    }

    @Autowired
    public void setUserRepository(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    @Autowired
    public void setPasswordEncoder(PasswordEncoder passwordEncoder){
        this.passwordEncoder = passwordEncoder;
    }

    @Autowired
    public void setJwtUtils(JwtUtils jwtUtils){
        this.jwtUtils = jwtUtils;
    }

}
