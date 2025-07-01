package com.example.creditproducts.service.CreditApplication;

import com.example.creditproducts.dto.CreditApplicationDTO;
import com.example.creditproducts.dto.CreditProductDTO;
import com.example.creditproducts.exception.CreditProductNotFoundException;
import com.example.creditproducts.exception.InvalidAmountException;
import com.example.creditproducts.exception.InvalidTermMonthsException;
import com.example.creditproducts.mapper.CreditProductMapper;
import com.example.creditproducts.model.CreditProduct;
import com.example.creditproducts.repository.CreditProductRepository;
import com.example.creditproducts.service.CreditProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
@RequiredArgsConstructor
public class CreditApplicationValidation {


    private CreditProductRepository creditProductRepository;
    private CreditProductMapper mapper;
    private ApplicationStatusValidation applicationStatusValidation;

    public CreditApplicationValidation(CreditProductRepository creditProductRepository,
                                       CreditProductMapper creditProductMapper,
                                       ApplicationStatusValidation applicationStatusValidation){
        mapper = creditProductMapper;
        this.applicationStatusValidation = applicationStatusValidation;
        this.creditProductRepository = creditProductRepository;
    }

    public void validate(CreditApplicationDTO creditApplicationDTO){
        if(creditApplicationDTO == null)
            throw new IllegalArgumentException();

        applicationStatusValidation.validateStatus(creditApplicationDTO.getStatus());

        CreditProduct creditProduct = creditProductRepository.findById(creditApplicationDTO.getCreditProductId()).
                orElseThrow(() -> new CreditProductNotFoundException(creditApplicationDTO.getCreditProductId()));


        validateAmount(creditApplicationDTO.getAmount(), mapper.toDTO(creditProduct));
        validateTerm(creditApplicationDTO.getTermMonths(), mapper.toDTO(creditProduct));
    }

    private void validateAmount(BigDecimal amount, CreditProductDTO product) {
        if (amount.compareTo(product.getMinAmount()) < 0 ||
                amount.compareTo(product.getMaxAmount()) > 0) {
            throw new InvalidAmountException(product.getMinAmount(), product.getMaxAmount());
        }
    }

    private void validateTerm(Integer termMonths, CreditProductDTO product) {
        if (termMonths < product.getTermMin() || termMonths > product.getTermMax()) {
            throw new InvalidTermMonthsException(product.getTermMin(), product.getTermMax());
        }
    }
}
