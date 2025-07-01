package com.example.creditproducts.service.CreditApplication;

import com.example.creditproducts.dto.CreditApplicationDTO;
import com.example.creditproducts.dto.CreditProductDTO;
import com.example.creditproducts.dto.IssuedLoanDTO;
import com.example.creditproducts.exception.ApplicationNotFoundException;
import com.example.creditproducts.exception.CreditProductNotFoundException;
import com.example.creditproducts.exception.InvalidAmountException;
import com.example.creditproducts.exception.InvalidTermMonthsException;
import com.example.creditproducts.mapper.CreditApplicationMapper;
import com.example.creditproducts.model.ApplicationStatus;
import com.example.creditproducts.model.CreditApplication;
import com.example.creditproducts.model.CreditProduct;
import com.example.creditproducts.repository.CreditApplicationRepository;
import com.example.creditproducts.repository.CreditProductRepository;
import com.example.creditproducts.service.CreditProductService;
import com.example.creditproducts.service.GenericService;
import com.example.creditproducts.service.IssuedLoanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CreditApplicationService extends GenericService<CreditApplication, CreditApplicationDTO> {

    CreditProductRepository creditProductRepository;
    CreditApplicationValidation creditApplicationValidator;
    IssuedLoanService issuedLoanService;
    ApplicationStatusValidation applicationStatusValidator;

    CreditProductService creditProductService;

    public CreditApplicationService(CreditApplicationRepository creditApplicationRepository,
                                    CreditApplicationMapper creditApplicationMapper,
                                    CreditProductRepository creditProductRepository,
                                    IssuedLoanService issuedLoanService,
                                    ApplicationStatusValidation applicationStatusValidation,
                                    CreditApplicationValidation creditApplicationValidation){
        repository = creditApplicationRepository;
        mapper = creditApplicationMapper;
        this.creditProductRepository = creditProductRepository;
        this.issuedLoanService = issuedLoanService;
        applicationStatusValidator = applicationStatusValidation;
        creditApplicationValidator = creditApplicationValidation;
    }

    public List<CreditApplicationDTO> getAllByClientid(Long id) {
        return mapper.toDTOs(((CreditApplicationRepository)repository).findByClientId(id).stream().toList());
    }

    public CreditApplicationDTO updateStatus(Long id, String newStatus){

        ApplicationStatus status = applicationStatusValidator.validateStatus(newStatus);

        CreditApplication application = repository.findById(id)
                .orElseThrow(() -> new ApplicationNotFoundException(id));

        application.setStatus(status);

        if (newStatus.equals("APPROVED")){
            IssuedLoanDTO newIssuedLoan = issuedLoanService.createLoan(mapper.toDTO(application));
        }

        return mapper.toDTO(repository.save(application));

    }

    public CreditApplicationDTO updateStatus(Long id, String newStatus, IssuedLoanService issuedLoanService){
        ApplicationStatus status = applicationStatusValidator.validateStatus(newStatus);

        CreditApplication application = repository.findById(id)
                .orElseThrow(() -> new ApplicationNotFoundException(id));

        application.setStatus(status);

        if (newStatus.equals("APPROVED")){
            IssuedLoanDTO newIssuedLoan = issuedLoanService.createLoan(mapper.toDTO(application));
        }

        return mapper.toDTO(repository.save(application));
    }

    @Override
    public CreditApplicationDTO create(CreditApplicationDTO creditApplicationDTO){
        creditApplicationValidator.validate(creditApplicationDTO);

        CreditApplication entity = mapper.toEntity(creditApplicationDTO);

        return mapper.toDTO(repository.save(entity));
    }


    public CreditApplicationDTO create(CreditApplicationDTO creditApplicationDTO, CreditProductDTO creditProductDTO){
        creditApplicationValidator.validate(creditApplicationDTO);

        CreditApplication entity = mapper.toEntity(creditApplicationDTO);
        return mapper.toDTO(repository.save(entity));
    }

}
