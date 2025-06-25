package com.example.creditproducts.controller;

import com.example.creditproducts.dto.ClientDTO;
import com.example.creditproducts.model.Client;
import com.example.creditproducts.model.CreditProduct;
import com.example.creditproducts.repository.ClientRepository;
import com.example.creditproducts.service.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
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
    public String createClient(@RequestBody Client client){
        clientRepository.save(client);
        return "Добавлен новый клиент";

    }

    @PutMapping("/update/{id}")
    public String updateClient(@PathVariable Long id, @RequestBody ClientDTO clientDTO){

        clientService.update(clientDTO, id);
        return "Данные клиента обновлены";

    }

    @Autowired
    public void setClientRepository(ClientRepository clientRepository){
        this.clientRepository = clientRepository;
    }


}
