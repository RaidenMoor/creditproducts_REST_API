package com.example.restbank.service;

import com.example.restbank.dto.IssuedLoanDTO;
import com.example.restbank.mapper.IssuedLoanMapper;
import com.example.restbank.model.IssuedLoan;
import com.example.restbank.repository.IssuedLoanRepository;
import org.springframework.stereotype.Service;

@Service
public class IssuedLoanService extends GenericService<IssuedLoan, IssuedLoanDTO> {
    public IssuedLoanService(IssuedLoanRepository issuedLoanRepository, IssuedLoanMapper issuedLoanMapper){
        repository = issuedLoanRepository;
        mapper = issuedLoanMapper;
    }
}
