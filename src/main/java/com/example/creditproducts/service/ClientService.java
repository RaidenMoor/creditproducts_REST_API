package com.example.restbank.service;

import com.example.restbank.dto.ClientDTO;
import com.example.restbank.mapper.ClientMapper;
import com.example.restbank.mapper.CreditProductMapper;
import com.example.restbank.model.Client;
import com.example.restbank.repository.ClientRepository;
import com.example.restbank.repository.CreditProductRepository;
import org.springframework.stereotype.Service;

@Service
public class ClientService extends GenericService<Client, ClientDTO>{

    public ClientService(ClientRepository clientRepository,
                         ClientMapper clientMapper){
        repository= clientRepository;
        mapper = clientMapper;
    }
}
