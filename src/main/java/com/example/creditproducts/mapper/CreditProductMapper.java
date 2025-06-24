package com.example.restbank.mapper;

import com.example.restbank.dto.CreditProductDTO;
import com.example.restbank.model.CreditProduct;
import org.springframework.stereotype.Component;

@Component
public class CreditProductMapper extends GenericMapper<CreditProduct, CreditProductDTO> {
    public CreditProductMapper() {
        super(CreditProduct.class, CreditProductDTO.class);
    }
}
