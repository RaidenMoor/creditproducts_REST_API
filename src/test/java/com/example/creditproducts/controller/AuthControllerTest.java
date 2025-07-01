package com.example.creditproducts.controller;

import com.example.creditproducts.controller.AuthController;
import com.example.creditproducts.dto.UserDTO;
import com.example.creditproducts.dto.security.JwtResponse;
import com.example.creditproducts.dto.security.LoginRequest;
import com.example.creditproducts.dto.security.RegisterRequest;
import com.example.creditproducts.jwt.JwtUtils;
import com.example.creditproducts.model.User;

import com.example.creditproducts.repository.RoleRepository;
import com.example.creditproducts.repository.UserRepository;
import com.example.creditproducts.security.CustomUserDetails;
import com.example.creditproducts.service.SecurityService;
import com.example.creditproducts.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AuthControllerTest {

    private MockMvc mockMvc;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtUtils jwtUtils;

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private UserService userService;

    @InjectMocks
    private AuthController authController;

    @Mock
    private Authentication authentication;

    @Mock
    private SecurityService securityService;

    @Mock
    private SecurityContext securityContext;

    private LoginRequest loginRequest;
    private RegisterRequest registerRequest;
    private UserDTO userDTO;
    private CustomUserDetails customUserDetails;


    @BeforeEach
    void setUp() {
        authController = new AuthController(userService, securityService);


        mockMvc = MockMvcBuilders.standaloneSetup(authController).build();

        loginRequest = new LoginRequest();
        loginRequest.setUsername("testuser");
        loginRequest.setPassword("password");

        registerRequest = new RegisterRequest();
        registerRequest.setUsername("newuser");
        registerRequest.setPassword("password");

        userDTO = new UserDTO();
        userDTO.setUsername("newuser");
        userDTO.setPassword("encodedPassword");


    }

    @Test
    void login_ValidCredentials_ReturnsJwtResponse() {

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(customUserDetails);
        when(jwtUtils.generateToken(customUserDetails)).thenReturn("testjwttoken");


        ResponseEntity<?> response = authController.login(loginRequest);


        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody() instanceof JwtResponse);
        assertEquals("testjwttoken", ((JwtResponse) response.getBody()).getToken());
        verify(authenticationManager, times(1)).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(jwtUtils, times(1)).generateToken(customUserDetails);
    }
    @Test
    void register_NewUser_ReturnsSuccessMessage() {

        when(userRepository.existsByUsername("newuser")).thenReturn(false);
        when(passwordEncoder.encode("password")).thenReturn("encodedPassword");
        when(userService.create(any(UserDTO.class))).thenReturn(userDTO);


        ResponseEntity<?> response = authController.register(registerRequest);


        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("User registered successfully!", response.getBody());
        verify(userRepository, times(1)).existsByUsername("newuser");
        verify(passwordEncoder, times(1)).encode("password");
        verify(userService, times(1)).create(any(UserDTO.class));
    }

    @Test
    void register_ExistingUser_ReturnsBadRequest() {

        when(userRepository.existsByUsername("newuser")).thenReturn(true);


        ResponseEntity<?> response = authController.register(registerRequest);


        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Error: Username is already taken!", response.getBody());
        verify(userRepository, times(1)).existsByUsername("newuser");
        verify(passwordEncoder, never()).encode(anyString());
        verify(userService, never()).create(any(UserDTO.class));
    }
}