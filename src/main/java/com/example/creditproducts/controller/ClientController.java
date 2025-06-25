package com.example.creditproducts.controller;

import com.example.creditproducts.dto.ClientDTO;
import com.example.creditproducts.model.Client;
import com.example.creditproducts.model.CreditProduct;
import com.example.creditproducts.repository.ClientRepository;
import com.example.creditproducts.service.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/clients")
public class ClientController {
    ClientRepository clientRepository;
    ClientService clientService;

    public ClientController(ClientService clientService){
        this.clientService = clientService;
    }

    @GetMapping
    public List<Client> getAllClients(){
        return clientRepository.findAll();
    }

    @PostMapping
    public ResponseEntity<?> createClient(@RequestBody Client client, BindingResult bindingResult){
        if (bindingResult.hasErrors()) {
            // Обработка ошибок валидации
            return new ResponseEntity<>(bindingResult.getAllErrors(), HttpStatus.BAD_REQUEST); // 400 Bad Request
        }

        try {
            Client savedClient = clientRepository.save(client);
            return new ResponseEntity<>("Добавлен новый клиент с id: " + savedClient.getId(), HttpStatus.CREATED); // 201 Created
        } catch (Exception e) {

            return new ResponseEntity<>("Ошибка при создании клиента: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR); // 500 Internal Server Error
        }

    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateClient(@PathVariable Long id, @RequestBody ClientDTO clientDTO,
                               BindingResult bindingResult){
        if (bindingResult.hasErrors()) {
            // Обработка ошибок валидации
            return new ResponseEntity<>(bindingResult.getAllErrors(), HttpStatus.BAD_REQUEST); // 400 Bad Request
        }
        try {
            clientService.update(clientDTO, id);
            return new ResponseEntity<>("Обновлены данные клиента с id: " + id, HttpStatus.OK);
        }
        catch (Exception e) {

            return new ResponseEntity<>("Ошибка при изменении данных клиента: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR); // 500 Internal Server Error
        }


    }

    @Autowired
    public void setClientRepository(ClientRepository clientRepository){
        this.clientRepository = clientRepository;
    }


}
