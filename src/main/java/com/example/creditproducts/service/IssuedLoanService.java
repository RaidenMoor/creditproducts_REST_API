package com.example.creditproducts.service;

import com.example.creditproducts.dto.CreditApplicationDTO;
import com.example.creditproducts.dto.CreditProductDTO;
import com.example.creditproducts.dto.IssuedLoanDTO;
import com.example.creditproducts.mapper.IssuedLoanMapper;
import com.example.creditproducts.model.IssuedLoan;
import com.example.creditproducts.repository.IssuedLoanRepository;
import jakarta.persistence.Access;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.time.LocalDate;

@Service
public class IssuedLoanService extends GenericService<IssuedLoan, IssuedLoanDTO> {
    public IssuedLoanService(IssuedLoanRepository issuedLoanRepository, IssuedLoanMapper issuedLoanMapper){
        repository = issuedLoanRepository;
        mapper = issuedLoanMapper;
    }

    @Autowired
    CreditProductService creditProductService;

    public IssuedLoanDTO createLoan(CreditApplicationDTO creditApplicationDTO){
        IssuedLoanDTO issuedLoanDTO = new IssuedLoanDTO();
        issuedLoanDTO.setCreditApplicationId(creditApplicationDTO.getId());
        issuedLoanDTO.setStartDate(LocalDate.now());
        LocalDate endDate = LocalDate.now().plusMonths(creditApplicationDTO.getTermMonths());
        issuedLoanDTO.setEndDate(endDate);
        BigDecimal monthlyAmount = calculateMonthlyAmount(creditApplicationDTO);
        issuedLoanDTO.setMonthlyPayment(monthlyAmount);
        issuedLoanDTO.setRemainingAmount(creditApplicationDTO.getAmount());

        IssuedLoan entity = mapper.toEntity(issuedLoanDTO);

        return mapper.toDTO(repository.save(entity));


    }

    public BigDecimal calculateMonthlyAmount(CreditApplicationDTO application){
        BigDecimal monthlyAmount;
        CreditProductDTO product = creditProductService.getById(application.getCreditProductId());

        BigDecimal m = product.getInterestRate().divide(BigDecimal.valueOf(1200), MathContext.DECIMAL128);
        BigDecimal onePlusM = BigDecimal.ONE.add(m);
        BigDecimal termMonths = BigDecimal.valueOf(application.getTermMonths());
        BigDecimal power = onePlusM.pow(termMonths.negate().intValue(), MathContext.DECIMAL128);
        BigDecimal oneMinusPower = BigDecimal.ONE.subtract(power);

        monthlyAmount = application.getAmount().multiply(m.divide(oneMinusPower, MathContext.DECIMAL128), MathContext.DECIMAL128);

        monthlyAmount = monthlyAmount.setScale(3, RoundingMode.HALF_UP);
        return  monthlyAmount;
    }

    public BigDecimal calculateMonthlyAmount(CreditApplicationDTO application, CreditProductDTO product){
        BigDecimal monthlyAmount;

        BigDecimal m = product.getInterestRate().divide(BigDecimal.valueOf(1200), MathContext.DECIMAL128);
        BigDecimal onePlusM = BigDecimal.ONE.add(m);
        BigDecimal termMonths = BigDecimal.valueOf(application.getTermMonths());
        BigDecimal power = onePlusM.pow(termMonths.negate().intValue(), MathContext.DECIMAL128);
        BigDecimal oneMinusPower = BigDecimal.ONE.subtract(power);

        monthlyAmount = application.getAmount().multiply(m.divide(oneMinusPower, MathContext.DECIMAL128), MathContext.DECIMAL128);

        monthlyAmount = monthlyAmount.setScale(3, RoundingMode.HALF_UP);
        return  monthlyAmount;
    }
    public IssuedLoanDTO createLoan(CreditApplicationDTO creditApplicationDTO, CreditProductDTO creditProductDTO){
        IssuedLoanDTO issuedLoanDTO = new IssuedLoanDTO();
        issuedLoanDTO.setCreditApplicationId(creditApplicationDTO.getId());
        issuedLoanDTO.setStartDate(LocalDate.now());
        LocalDate endDate = LocalDate.now().plusMonths(creditApplicationDTO.getTermMonths());
        issuedLoanDTO.setEndDate(endDate);
        BigDecimal monthlyAmount = calculateMonthlyAmount(creditApplicationDTO, creditProductDTO);
        issuedLoanDTO.setMonthlyPayment(monthlyAmount);
        issuedLoanDTO.setRemainingAmount(creditApplicationDTO.getAmount());

        IssuedLoan entity = mapper.toEntity(issuedLoanDTO);

        return mapper.toDTO(repository.save(entity));


    }

}
