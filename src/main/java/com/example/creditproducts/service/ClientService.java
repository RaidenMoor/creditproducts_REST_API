package com.example.creditproducts.service;

import com.example.creditproducts.dto.ClientDTO;
import com.example.creditproducts.mapper.ClientMapper;
import com.example.creditproducts.mapper.CreditProductMapper;
import com.example.creditproducts.model.Client;
import com.example.creditproducts.repository.ClientRepository;
import com.example.creditproducts.repository.CreditProductRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class ClientService extends GenericService<Client, ClientDTO>{

    public ClientService(ClientRepository clientRepository,
                         ClientMapper clientMapper){
        repository= clientRepository;
        mapper = clientMapper;
    }

//    @Override
//    public ClientDTO update(ClientDTO clientDTO, Long id){
//        Client entity = mapper.toEntity(clientDTO);
//        LocalDate registerDate = clientDTO.getRegistrationDate();
//
//        if (id != null) {
//            Client existingEntity = repository.findById(id).orElse(null);
//            if (existingEntity != null) {
//                entity.setId(id);
//                return mapper.toDTO(repository.save(entity));
//            }
//        }
//        return null;
//    }
}
