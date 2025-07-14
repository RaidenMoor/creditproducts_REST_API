package com.example.creditproducts.service;

import com.example.creditproducts.dto.ClientDTO;
import com.example.creditproducts.mapper.ClientMapper;
import com.example.creditproducts.model.Client;
import com.example.creditproducts.repository.ClientRepository;

import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class ClientService extends GenericService<Client, ClientDTO>{

    public ClientService(ClientRepository clientRepository,
                         ClientMapper clientMapper){
        repository= clientRepository;
        mapper = clientMapper;
    }


}
