package com.example.restbank.service;

import com.example.restbank.dto.CreditProductDTO;
import com.example.restbank.mapper.CreditProductMapper;
import com.example.restbank.model.CreditProduct;
import com.example.restbank.repository.CreditProductRepository;
import org.springframework.stereotype.Service;

@Service
public class CreditProductService extends GenericService<CreditProduct, CreditProductDTO> {

    public CreditProductService(CreditProductRepository creditProductRepository,
                                CreditProductMapper creditProductMapper){
        repository= creditProductRepository;
        mapper = creditProductMapper;
    }
}
