package com.example.creditproducts.service;

import com.example.creditproducts.dto.CreditApplicationDTO;
import com.example.creditproducts.dto.CreditProductDTO;
import com.example.creditproducts.dto.IssuedLoanDTO;
import com.example.creditproducts.exception.InvalidAmountException;
import com.example.creditproducts.exception.InvalidTermMonthsException;
import com.example.creditproducts.mapper.CreditApplicationMapper;
import com.example.creditproducts.model.ApplicationStatus;
import com.example.creditproducts.model.CreditApplication;
import com.example.creditproducts.repository.CreditApplicationRepository;
import com.example.creditproducts.repository.CreditProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CreditApplicationService extends GenericService<CreditApplication, CreditApplicationDTO> {
    public CreditApplicationService(CreditApplicationRepository creditApplicationRepository,
                                    CreditApplicationMapper creditApplicationMapper){
        repository = creditApplicationRepository;
        mapper = creditApplicationMapper;
    }

    @Autowired
    IssuedLoanService issuedLoanService;

    @Autowired
    CreditProductService creditProductService;

    public List<CreditApplicationDTO> getAllByClientid(Long id) {
        return mapper.toDTOs(((CreditApplicationRepository)repository).findByClientId(id).stream().toList());
    }

    public CreditApplicationDTO updateStatus(Long id, String newStatus){
        CreditApplication existingEntity = repository.findById(id).orElse(null);
        if (existingEntity != null) {
            existingEntity.setStatus(ApplicationStatus.valueOf(newStatus));
            if (newStatus.equals("APPROVED")){
                IssuedLoanDTO newIssuedLoan = issuedLoanService.createLoan(mapper.toDTO(existingEntity));

            }
            return mapper.toDTO(repository.save(existingEntity));
        }
        else return null;
    }

    public CreditApplicationDTO updateStatus(Long id, String newStatus, IssuedLoanService issuedLoanServiceOver){
        CreditApplication existingEntity = repository.findById(id).orElse(null);
        if (existingEntity != null) {
            existingEntity.setStatus(ApplicationStatus.valueOf(newStatus));
            if (newStatus.equals("APPROVED")){
                IssuedLoanDTO newIssuedLoan = issuedLoanServiceOver.createLoan(mapper.toDTO(existingEntity));

            }
            return mapper.toDTO(repository.save(existingEntity));
        }
        else return null;
    }

    @Override
    public CreditApplicationDTO create(CreditApplicationDTO creditApplicationDTO){
        if (creditApplicationDTO == null) return null;
        CreditApplication entity = mapper.toEntity(creditApplicationDTO);
        CreditProductDTO creditProductDTO = creditProductService.getById(creditApplicationDTO.getCreditProductId());
        if(creditApplicationDTO.getAmount().compareTo(creditProductDTO.getMaxAmount()) >= 0 ||
                creditApplicationDTO.getAmount().compareTo(creditProductDTO.getMinAmount()) <= 0){
            throw new InvalidAmountException(creditProductDTO.getMinAmount(),creditProductDTO.getMaxAmount());
        }
        if(creditApplicationDTO.getTermMonths() <= creditProductDTO.getTermMin() ||
                creditApplicationDTO.getTermMonths() >= creditProductDTO.getTermMax()){
            throw new InvalidTermMonthsException(creditProductDTO.getTermMin(), creditProductDTO.getTermMax());

        }
        Long entityId = entity.getId();
        if (entityId != null && repository.existsById(entityId)) {
            return null;
        }
        return mapper.toDTO(repository.save(entity));
    }


    public CreditApplicationDTO create(CreditApplicationDTO creditApplicationDTO, CreditProductDTO creditProductDTO){
        if (creditApplicationDTO == null) return null;
        CreditApplication entity = mapper.toEntity(creditApplicationDTO);
        if(creditApplicationDTO.getAmount().compareTo(creditProductDTO.getMaxAmount()) >= 0 ||
                creditApplicationDTO.getAmount().compareTo(creditProductDTO.getMinAmount()) <= 0){
            throw new InvalidAmountException(creditProductDTO.getMinAmount(),creditProductDTO.getMaxAmount());
        }
        if(creditApplicationDTO.getTermMonths() <= creditProductDTO.getTermMin() ||
                creditApplicationDTO.getTermMonths() >= creditProductDTO.getTermMax()){
            throw new InvalidTermMonthsException(creditProductDTO.getTermMin(), creditProductDTO.getTermMax());

        }
        Long entityId = entity.getId();
        if (entityId != null && repository.existsById(entityId)) {
            return null;
        }
        return mapper.toDTO(repository.save(entity));
    }
}
