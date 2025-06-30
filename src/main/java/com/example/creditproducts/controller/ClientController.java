package com.example.creditproducts.controller;

import com.example.creditproducts.dto.ClientDTO;
import com.example.creditproducts.exception.AccessException;
import com.example.creditproducts.exception.ApplicationNotFoundException;
import com.example.creditproducts.exception.ClientNotFoundException;
import com.example.creditproducts.exception.DublicateException;
import com.example.creditproducts.model.Client;
import com.example.creditproducts.model.CreditApplication;
import com.example.creditproducts.model.CreditProduct;
import com.example.creditproducts.model.User;
import com.example.creditproducts.repository.ClientRepository;
import com.example.creditproducts.repository.UserRepository;
import com.example.creditproducts.security.CustomUserDetails;
import com.example.creditproducts.service.ClientService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@RestController
@RequestMapping("/clients")
public class ClientController {
    ClientRepository clientRepository;
    ClientService clientService;

    @Autowired
    UserRepository userRepository;

    public ClientController(ClientService clientService){
        this.clientService = clientService;
    }

    @GetMapping
    @Operation(summary = "Просмотр клиентов")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Список клиентов предоставлен"),
            @ApiResponse(responseCode = "403", description = "Нет прав доступа"),
    })
    public List<ClientDTO> getAllClients(){
        return clientService.getAll();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Просмотреть данные клиента")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Клиент найден"),
            @ApiResponse(responseCode = "403", description = "Нет прав доступа"),
            @ApiResponse(responseCode = "404", description = "Клиент не найден")
    })
    public ClientDTO getClient(@PathVariable Long id){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Optional<Client> clientOptional = clientRepository.findById(id);
        if (clientOptional.isEmpty()) {
            throw new ClientNotFoundException(id);
        }

        Client client = clientOptional.get();

        if (authentication != null && authentication.isAuthenticated()) {

            Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
            boolean isUser = authorities.stream()
                    .anyMatch(authority -> authority.getAuthority().equals("ROLE_USER"));

            if (isUser) {

                String currentUsername = authentication.getName();
                if (client.getUser() == null || !Objects.equals(client.getUser().getUsername(), currentUsername)) { // Проверяем владельца
                    throw new AccessException();
                }
            }
        }
        return clientService.getById(id);

    }

    @PostMapping
    @Operation(summary = "Добавить клиента")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Клиент добавлен"),
            @ApiResponse(responseCode = "403", description = "Нет прав доступа"),
            @ApiResponse(responseCode = "400", description = "Некорректно введены данные"),
            @ApiResponse(responseCode = "409", description = "Попытка добавления дублирующей записи")
    })
    public ResponseEntity<?> createClient(@Valid @RequestBody Client client, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            // Обработка ошибок валидации
            return new ResponseEntity<>(bindingResult.getAllErrors(), HttpStatus.BAD_REQUEST);
        }

        if(clientRepository.existsByEmail(client.getEmail())){
            throw new DublicateException("Пользователь с таким email уже существует");
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            // Проверяем роль пользователя
            Collection<?> authorities = authentication.getAuthorities();
            boolean isUser = authorities.stream()
                    .anyMatch(authority -> authority.toString().equals("ROLE_USER"));

            if (isUser) {
                Object principal = authentication.getPrincipal();

                if (principal instanceof String) {
                    String username = (String) principal;
                    User user = userRepository.findByUsername(username).orElse(null);
                    if (user != null) {
                        client.setUser(user);
                    }
                }


            }
        }


        Client savedClient = clientRepository.save(client);
        return new ResponseEntity<>("Добавлен новый клиент с id: " + savedClient.getId(), HttpStatus.CREATED); // 201 Created

    }

    @PutMapping("/update/{id}")
    @Operation(summary = "Добавить клиента")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Данные обновлены"),
            @ApiResponse(responseCode = "403", description = "Нет прав доступа"),
            @ApiResponse(responseCode = "400", description = "Некорректно введены данные"),
            @ApiResponse(responseCode = "404", description = "Клиент не найден")
    })
    public ResponseEntity<?> updateClient(@PathVariable Long id,@Valid @RequestBody ClientDTO clientDTO,
                               BindingResult bindingResult){
        if (bindingResult.hasErrors()) {
            return new ResponseEntity<>(bindingResult.getAllErrors(), HttpStatus.BAD_REQUEST);
        }
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Optional<Client> clientOptional = clientRepository.findById(id);
        if (clientOptional.isEmpty()) {
            throw new ClientNotFoundException(id);
        }

        Client client = clientOptional.get();

        if (authentication != null && authentication.isAuthenticated()) {

            Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
            boolean isUser = authorities.stream()
                    .anyMatch(authority -> authority.getAuthority().equals("ROLE_USER"));

            if (isUser) {

                String currentUsername = authentication.getName();
                if (client.getUser() == null || !Objects.equals(client.getUser().getUsername(), currentUsername)) { // Проверяем владельца
                    throw new AccessException();
                }
            }
        }
            clientService.update(clientDTO, id);
            return new ResponseEntity<>("Обновлены данные клиента с id: " + id, HttpStatus.OK);



    }

    @Autowired
    public void setClientRepository(ClientRepository clientRepository){
        this.clientRepository = clientRepository;
    }


}
