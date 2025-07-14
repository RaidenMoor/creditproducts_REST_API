package com.example.creditproducts.service;

import com.example.creditproducts.dto.UserDTO;
import com.example.creditproducts.dto.security.LoginRequest;
import com.example.creditproducts.dto.security.RegisterRequest;
import com.example.creditproducts.exception.AccessException;
import com.example.creditproducts.exception.ClientNotFoundException;
import com.example.creditproducts.jwt.JwtUtils;
import com.example.creditproducts.model.Client;
import com.example.creditproducts.model.User;
import com.example.creditproducts.repository.ClientRepository;
import com.example.creditproducts.repository.UserRepository;
import com.example.creditproducts.security.CustomUserDetails;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Objects;
import java.util.Optional;

@Service
public class SecurityService {

    private final UserRepository userRepository;
    private final UserService userService;
    private final ClientRepository clientRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;

    public SecurityService(UserRepository userRepository, ClientRepository clientRepository,
                           PasswordEncoder passwordEncoder, UserService userService,
                           AuthenticationManager authenticationManager, JwtUtils jwtUtils){
        this.userRepository = userRepository;
        this.clientRepository = clientRepository;
        this.passwordEncoder = passwordEncoder;
        this.userService = userService;
        this.authenticationManager=authenticationManager;
        this.jwtUtils = jwtUtils;

    }

    public Long userRoleCheck(Authentication authentication){
        Collection<?> authorities = authentication.getAuthorities();
        boolean isUser = authorities.stream()
                .anyMatch(authority -> authority.toString().equals("ROLE_USER"));

        if (isUser) {
            Object principal = authentication.getPrincipal();

            if (principal instanceof String) {
                String username = (String) principal;
                User user = userRepository.findByUsername(username).orElse(null);
                if (user != null) {
                    return user.getId();
                }
                else return 0L;
            }

        }
        else return 0L;

        return 0L;
    }

    public boolean userRoleCheck(Authentication authentication, Long id){
        Client client = existingClientCheck(id);
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        boolean isUser = authorities.stream()
                .anyMatch(authority -> authority.getAuthority().equals("ROLE_USER"));

        if (isUser) {

            String currentUsername = authentication.getName();
            if (client.getUser() == null || !Objects.equals(client.getUser().getUsername(), currentUsername)) { // Проверяем владельца
                throw new AccessException();
            }
            return true;
        }
        return false;
    }

    public Client existingClientCheck(Long id){
        Optional<Client> clientOptional = clientRepository.findById(id);
        if (clientOptional.isEmpty()) {
            throw new ClientNotFoundException(id);
        }

        return clientOptional.get();
    }

    public ResponseEntity<?> createNewUser(RegisterRequest request){

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

    public String loginUser(LoginRequest request){
        if(!userRepository.existsByUsername(request.getUsername())&& !request.getUsername().equals("admin"))
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
        return jwt;
    }
}
