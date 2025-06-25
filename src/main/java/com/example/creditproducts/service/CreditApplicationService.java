package com.example.creditproducts.service;

import com.example.creditproducts.dto.CreditApplicationDTO;
import com.example.creditproducts.mapper.CreditApplicationMapper;
import com.example.creditproducts.model.ApplicationStatus;
import com.example.creditproducts.model.CreditApplication;
import com.example.creditproducts.repository.CreditApplicationRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CreditApplicationService extends GenericService<CreditApplication, CreditApplicationDTO> {
    public CreditApplicationService(CreditApplicationRepository creditApplicationRepository,
                                    CreditApplicationMapper creditApplicationMapper){
        repository = creditApplicationRepository;
        mapper = creditApplicationMapper;
    }

    public List<CreditApplicationDTO> getAllByClientid(Long id) {
        return mapper.toDTOs(((CreditApplicationRepository)repository).findByClientId(id).stream().toList());
    }

    public CreditApplicationDTO updateStatus(Long id, String newStatus){
        CreditApplication existingEntity = repository.findById(id).orElse(null);
        if (existingEntity != null) {
            existingEntity.setStatus(ApplicationStatus.valueOf(newStatus));
            return mapper.toDTO(repository.save(existingEntity));
        }
        else return null;
    }
}
