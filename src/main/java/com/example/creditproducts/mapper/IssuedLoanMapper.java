package com.example.creditproducts.mapper;


import com.example.creditproducts.dto.IssuedLoanDTO;
import com.example.creditproducts.model.CreditApplication;
import com.example.creditproducts.model.IssuedLoan;
import com.example.creditproducts.repository.CreditApplicationRepository;
import com.example.creditproducts.repository.IssuedLoanRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class IssuedLoanMapper extends GenericMapper<IssuedLoan, IssuedLoanDTO>
        implements ConverterForSpecificFields<IssuedLoan, IssuedLoanDTO>{
    public IssuedLoanMapper() {
        super(IssuedLoan.class, IssuedLoanDTO.class);
    }

    IssuedLoanRepository issuedLoanRepository;
    CreditApplicationRepository creditApplicationRepository;

    @PostConstruct
    @Override
    public void setupMapper() {
        modelMapper.createTypeMap(IssuedLoan.class, IssuedLoanDTO.class)
                .addMappings(m -> {
                    m.skip(IssuedLoanDTO::setCreditApplicationId);
                })
                .setPostConverter(toDtoConverter());

        modelMapper.createTypeMap(IssuedLoanDTO.class, IssuedLoan.class)
                .addMappings(m -> {
                    m.skip(IssuedLoan::setCreditApplication);

                })
                .setPostConverter(toEntityConverter());
    }

    @Override
    public void mapSpecificFields(IssuedLoanDTO source, IssuedLoan destination) {
        Long creditApplicationId = source.getCreditApplicationId();
        if (creditApplicationId != null) {
            destination.setCreditApplication(creditApplicationRepository.
                    findById(creditApplicationId).orElse(null));
        } else destination.setCreditApplication(null);

    }

    @Override
    public void mapSpecificFields(IssuedLoan source, IssuedLoanDTO destination) {
        Long creditApplicationId = null;
        CreditApplication creditApplication = source.getCreditApplication();
        if (creditApplication != null) {
            creditApplicationId = creditApplication.getId();
        }
        destination.setCreditApplicationId(creditApplicationId);
    }

    @Autowired
    public void setCreditApplicationRepository(CreditApplicationRepository creditApplicationRepository){
        this.creditApplicationRepository=creditApplicationRepository;
    }

    @Autowired
    public void setIssuedLoanRepository(IssuedLoanRepository issuedLoanRepository){
        this.issuedLoanRepository = issuedLoanRepository;
    }
}
