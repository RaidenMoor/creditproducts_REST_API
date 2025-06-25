package com.example.creditproducts.service;

import com.example.creditproducts.dto.CreditProductDTO;
import com.example.creditproducts.mapper.CreditProductMapper;
import com.example.creditproducts.model.CreditProduct;
import com.example.creditproducts.repository.CreditProductRepository;
import org.springframework.stereotype.Service;

@Service
public class CreditProductService extends GenericService<CreditProduct, CreditProductDTO> {

    public CreditProductService(CreditProductRepository creditProductRepository,
                                CreditProductMapper creditProductMapper){
        repository= creditProductRepository;
        mapper = creditProductMapper;
    }
}
