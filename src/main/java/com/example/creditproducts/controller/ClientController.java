package com.example.creditproducts.controller;

import com.example.creditproducts.dto.ClientDTO;
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
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.List;
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
    public List<Client> getAllClients(){
        return clientRepository.findAll();
    }

    @GetMapping("/{id}")
    public ClientDTO getClient(@PathVariable Long id){
        Optional<Client> client = clientRepository.findById(id);
        if (client.isEmpty()) {
            throw new ApplicationNotFoundException(id);
        }
        return clientService.getById(id);

    }

    @PostMapping
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
                    String username = (String) principal; // Теперь можно использовать
                    User user = userRepository.findByUsername(username).orElse(null); // Используем orElse(null) для безопасной обработки
                    if (user != null) {
                        client.setUser(user); //  Устанавливаем пользователя
                    }
                }


            }
        }


        Client savedClient = clientRepository.save(client);
        return new ResponseEntity<>("Добавлен новый клиент с id: " + savedClient.getId(), HttpStatus.CREATED); // 201 Created

    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateClient(@PathVariable Long id,@Valid @RequestBody ClientDTO clientDTO,
                               BindingResult bindingResult){
        if (bindingResult.hasErrors()) {
            return new ResponseEntity<>(bindingResult.getAllErrors(), HttpStatus.BAD_REQUEST);
        }
        Optional<Client> client = clientRepository.findById(id);
        if (client.isEmpty()) {
            throw new ClientNotFoundException(id);
        }
            clientService.update(clientDTO, id);
            return new ResponseEntity<>("Обновлены данные клиента с id: " + id, HttpStatus.OK);



    }

    @Autowired
    public void setClientRepository(ClientRepository clientRepository){
        this.clientRepository = clientRepository;
    }


}
