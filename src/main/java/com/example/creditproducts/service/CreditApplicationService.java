package com.example.creditproducts.service;

import com.example.creditproducts.dto.CreditApplicationDTO;
import com.example.creditproducts.mapper.CreditApplicationMapper;
import com.example.creditproducts.model.CreditApplication;
import com.example.creditproducts.repository.CreditApplicationRepository;
import org.springframework.stereotype.Service;

@Service
public class CreditApplicationService extends GenericService<CreditApplication, CreditApplicationDTO> {
    public CreditApplicationService(CreditApplicationRepository creditApplicationRepository,
                                    CreditApplicationMapper creditApplicationMapper){
        repository = creditApplicationRepository;
        mapper = creditApplicationMapper;
    }
}
