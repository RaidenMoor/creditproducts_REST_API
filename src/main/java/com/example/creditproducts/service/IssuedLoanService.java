package com.example.creditproducts.service;

import com.example.creditproducts.dto.IssuedLoanDTO;
import com.example.creditproducts.mapper.IssuedLoanMapper;
import com.example.creditproducts.model.IssuedLoan;
import com.example.creditproducts.repository.IssuedLoanRepository;
import org.springframework.stereotype.Service;

@Service
public class IssuedLoanService extends GenericService<IssuedLoan, IssuedLoanDTO> {
    public IssuedLoanService(IssuedLoanRepository issuedLoanRepository, IssuedLoanMapper issuedLoanMapper){
        repository = issuedLoanRepository;
        mapper = issuedLoanMapper;
    }
}
