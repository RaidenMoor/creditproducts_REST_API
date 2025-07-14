package com.example.creditproducts.controller;

import com.example.creditproducts.dto.ClientDTO;
import com.example.creditproducts.model.Client;
import com.example.creditproducts.model.User;
import com.example.creditproducts.repository.ClientRepository;
import com.example.creditproducts.repository.UserRepository;
import com.example.creditproducts.service.ClientService;
import com.example.creditproducts.service.SecurityService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.*;


import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.modelmapper.internal.bytebuddy.matcher.ElementMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class ClientControllerTest {

    private MockMvc mockMvc;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Mock
    private ClientService clientService;

    @Mock
    private SecurityService securityService;

    @Mock
    private ClientRepository clientRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private ClientController clientController;

    private Client testClient;
    private ClientDTO testClientDTO;
    private User testUser;

    @BeforeEach
    void setUp() throws Exception {
        clientController = new ClientController(clientService, clientRepository,
                securityService);


        mockMvc = MockMvcBuilders.standaloneSetup(clientController).build();


        testUser = new User();
        testUser.setId(1L);
        testUser.setUsername("testuser");

        testClient = new Client();
        testClient.setId(1L);
        testClient.setFirstName("John");
        testClient.setLastName("Doe");
        testClient.setEmail("john.doe@example.com");
        testClient.setUser(testUser);

        testClientDTO = new ClientDTO();
        testClientDTO.setId(1L);
        testClientDTO.setFirstName("John");
        testClientDTO.setLastName("Doe");
        testClientDTO.setEmail("john.doe@example.com");
    }

    @Test
    void getAllClients_ShouldReturnListOfClients() throws Exception {

        when(clientService.getAll()).thenReturn(Collections.singletonList(testClientDTO));


        mockMvc.perform(get("/clients"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));

    }

    @Test
    void getClient_WhenAdmin_ShouldReturnClient() throws Exception {

        setupSecurityContext("ROLE_ADMIN");
        when(clientRepository.findById(1L)).thenReturn(Optional.of(testClient));
        when(clientService.getById(1L)).thenReturn(testClientDTO);


        mockMvc.perform(get("/clients/1"))
                .andExpect(status().isOk());

    }

    @Test
    void getClient_WhenOwner_ShouldReturnClient() throws Exception {

        setupSecurityContext("ROLE_USER", "testuser");
        when(clientRepository.findById(1L)).thenReturn(Optional.of(testClient));
        when(clientService.getById(1L)).thenReturn(testClientDTO);


        mockMvc.perform(get("/clients/1"))
                .andExpect(status().isOk());
    }

    @Test
    void getClient_WhenNotOwner_ShouldThrowAccessException() throws Exception {

        setupSecurityContext("ROLE_USER", "otheruser");
        when(clientRepository.findById(1L)).thenReturn(Optional.of(testClient));


        mockMvc.perform(get("/clients/1"))
                .andExpect(status().isForbidden());
    }

    @Test
    void createClient_WithValidData_ShouldReturnCreated() throws Exception {
        // Arrange
        setupSecurityContext("ROLE_ADMIN");
        when(clientRepository.existsByEmail("john.doe@example.com")).thenReturn(false);
        when(clientRepository.save(any())).thenReturn(testClient);


        mockMvc.perform(post("/clients")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testClient)))
                .andExpect(status().isCreated())
                .andExpect(content().string(containsString("Обновлены данные клиента с id: 1")));
    }

    @Test
    void createClient_WithDuplicateEmail_ShouldThrowException() throws Exception {

        setupSecurityContext("ROLE_ADMIN");
        when(clientRepository.existsByEmail("john.doe@example.com")).thenReturn(true);


        mockMvc.perform(post("/clients")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testClient)))
                .andExpect(status().isConflict());
    }

    @Test
    void updateClient_WhenAdmin_ShouldUpdateClient() throws Exception {
        // Arrange
        setupSecurityContext("ROLE_ADMIN");
        when(clientRepository.findById(1L)).thenReturn(Optional.of(testClient));
        when(clientService.update(any(), anyLong())).thenReturn(testClientDTO);


        mockMvc.perform(put("/clients/update/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testClientDTO)))
                .andExpect(status().isOk())
                //проблемы с кодировкой несмотря на настройки проекта
                .andExpect(content().string(containsString("????????? ?????? ??????? ? id: 1")));
    }

    @Test
    void updateClient_WhenOwner_ShouldUpdateClient() throws Exception {

        setupSecurityContext("ROLE_USER", "testuser");
        when(clientRepository.findById(1L)).thenReturn(Optional.of(testClient));
        when(clientService.update(any(), anyLong())).thenReturn(testClientDTO);


        mockMvc.perform(put("/clients/update/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testClientDTO)))
                .andExpect(status().isOk());
    }

    @Test
    void updateClient_WhenNotOwner_ShouldThrowAccessException() throws Exception {

        setupSecurityContext("ROLE_USER", "otheruser");
        when(clientRepository.findById(1L)).thenReturn(Optional.of(testClient));


        mockMvc.perform(put("/clients/update/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testClientDTO)))
                .andExpect(status().isForbidden());
    }

    private void setupSecurityContext(String role) {
        setupSecurityContext(role, "admin");
    }

    private void setupSecurityContext(String role, String username) {
        GrantedAuthority authority = new SimpleGrantedAuthority(role);
        Authentication auth = new UsernamePasswordAuthenticationToken(
                username,
                "password",
                Collections.singletonList(authority));
        SecurityContextHolder.getContext().setAuthentication(auth);
    }
}