package com.example.restbank.service;

import com.example.restbank.dto.CreditApplicationDTO;
import com.example.restbank.mapper.CreditApplicationMapper;
import com.example.restbank.model.CreditApplication;
import com.example.restbank.repository.CreditApplicationRepository;
import org.springframework.stereotype.Service;

@Service
public class CreditApplicationService extends GenericService<CreditApplication, CreditApplicationDTO> {
    public CreditApplicationService(CreditApplicationRepository creditApplicationRepository,
                                    CreditApplicationMapper creditApplicationMapper){
        repository = creditApplicationRepository;
        mapper = creditApplicationMapper;
    }
}
