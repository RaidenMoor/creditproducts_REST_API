package com.example.creditproducts.mapper;


import com.example.creditproducts.dto.CreditProductDTO;
import com.example.creditproducts.model.CreditProduct;
import org.springframework.stereotype.Component;

@Component
public class CreditProductMapper extends GenericMapper<CreditProduct, CreditProductDTO> {
    public CreditProductMapper() {
        super(CreditProduct.class, CreditProductDTO.class);
    }
}
