package com.example.creditproducts.service;

import com.example.creditproducts.dto.CreditApplicationDTO;
import com.example.creditproducts.dto.CreditProductDTO;
import com.example.creditproducts.dto.IssuedLoanDTO;
import com.example.creditproducts.mapper.CreditProductMapper;
import com.example.creditproducts.mapper.IssuedLoanMapper;
import com.example.creditproducts.model.CreditApplication;
import com.example.creditproducts.model.IssuedLoan;
import com.example.creditproducts.repository.IssuedLoanRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class IssuedLoanServiceTest {

    @Mock
    private IssuedLoanRepository issuedLoanRepository;

    @Mock
    private IssuedLoanMapper issuedLoanMapper;

    @Mock
    CreditProductMapper creditProductMapper;

    @Mock
    private CreditProductService creditProductService;

    @InjectMocks
    private IssuedLoanService issuedLoanService;

    private CreditApplicationDTO testApplicationDto;
    private CreditApplication testApplication;
    private CreditProductDTO testProduct;
    private IssuedLoan testEntity;
    private IssuedLoanDTO testLoanDTO;

    @BeforeEach
    void setUp() {
        // Подготовка тестовых данных
        testApplicationDto = new CreditApplicationDTO();
        testApplicationDto.setId(1L);
        testApplicationDto.setAmount(new BigDecimal("10000.00"));
        testApplicationDto.setTermMonths(12);
        testApplicationDto.setCreditProductId(1L);

        testApplication = new CreditApplication();
        testApplication.setId(1L);
        testApplication.setAmount(new BigDecimal("10000.00"));
        testApplication.setTermMonths(12);
        testApplication.setCreditProduct(creditProductMapper.toEntity(testProduct));


        testProduct = new CreditProductDTO();
        testProduct.setId(1L);
        testProduct.setInterestRate(new BigDecimal("12.00")); // 12% годовых

        testEntity = new IssuedLoan();
        testEntity.setId(1L);
        testEntity.setCreditApplication(testApplication);
        testEntity.setStartDate(LocalDate.now());
        testEntity.setEndDate(LocalDate.now().plusMonths(12));
        testEntity.setMonthlyPayment(new BigDecimal("888.492"));
        testEntity.setRemainingAmount(new BigDecimal("10000.00"));

        testLoanDTO = new IssuedLoanDTO();
        testLoanDTO.setId(1L);
        testLoanDTO.setCreditApplicationId(1L);
        testLoanDTO.setStartDate(LocalDate.now());
        testLoanDTO.setEndDate(LocalDate.now().plusMonths(12));
        testLoanDTO.setMonthlyPayment(new BigDecimal("888.492"));
        testLoanDTO.setRemainingAmount(new BigDecimal("10000.00"));
    }

    @Test
    void createLoan_ShouldCreateCorrectLoan() {


        when(issuedLoanMapper.toEntity(any(IssuedLoanDTO.class)))
                .thenReturn(testEntity);
        when(issuedLoanRepository.save(testEntity))
                .thenReturn(testEntity);
        when(issuedLoanMapper.toDTO(testEntity))
                .thenReturn(testLoanDTO);


        IssuedLoanDTO result = issuedLoanService.createLoan(testApplicationDto, testProduct);


        assertNotNull(result);
        assertEquals(testApplication.getId(), result.getCreditApplicationId());
        assertEquals(LocalDate.now(), result.getStartDate());
        assertEquals(LocalDate.now().plusMonths(12), result.getEndDate());
        assertEquals(new BigDecimal("10000.00"), result.getRemainingAmount());


        assertEquals(0, new BigDecimal("888.492").compareTo(result.getMonthlyPayment()));


        verify(issuedLoanMapper).toEntity(any(IssuedLoanDTO.class));
        verify(issuedLoanRepository).save(testEntity);
        verify(issuedLoanMapper).toDTO(testEntity);
    }

    @Test
    void calculateMonthlyAmount_ShouldCalculateCorrectly() {


        // Ожидаемый расчет:
        // m = 12% / 1200 = 0.01
        // 1 + m = 1.01
        // (1 + m)^-n = 1.01^-12 ≈ 0.887449
        // 1 - (1 + m)^-n ≈ 0.112551
        // Ежемесячный платеж = 10000 * (0.01 / 0.112551) ≈ 888.488

        BigDecimal result = issuedLoanService.calculateMonthlyAmount(testApplicationDto, testProduct);


        assertNotNull(result);
        assertEquals(0, new BigDecimal("888.488").compareTo(result));

    }

    @Test
    void calculateMonthlyAmount_WithDifferentParameters_ShouldCalculateCorrectly() {
        CreditApplicationDTO application = new CreditApplicationDTO();
        application.setCreditProductId(2L);
        application.setAmount(new BigDecimal("50000.00"));
        application.setTermMonths(24);

        CreditProductDTO product = new CreditProductDTO();
        product.setId(2L);
        product.setInterestRate(new BigDecimal("9.50")); // 9.5% годовых



        // Ожидаемый расчет:
        // m = 9.5% / 1200 ≈ 0.007916667
        // (1 + m)^-n ≈ (1.007916667)^-24 ≈ 0.827849
        // 1 - (1 + m)^-n ≈ 0.172151
        // Ежемесячный платеж = 50000 * (0.007916667 / 0.172151) ≈ 2353.674

        BigDecimal result = issuedLoanService.calculateMonthlyAmount(application, testProduct);

        assertNotNull(result);
        assertEquals(0, new BigDecimal("2353.674").compareTo(result.setScale(3, RoundingMode.HALF_UP)));
    }

    @Test
    void createLoan_WithNullInput_ShouldThrowException() {

        assertThrows(NullPointerException.class, () -> {
            issuedLoanService.createLoan(null);
        });
    }

    @Test
    void calculateMonthlyAmount_WithNullInput_ShouldThrowException() {

        assertThrows(NullPointerException.class, () -> {
            issuedLoanService.calculateMonthlyAmount(null);
        });
    }

    @Test
    void createLoan_ShouldSetCorrectDates() {
        testApplicationDto.setTermMonths(36); // 3 года

        IssuedLoanDTO result = issuedLoanService.createLoan(testApplicationDto, testProduct);

        assertNotNull(result);
        assertEquals(LocalDate.now(), result.getStartDate());
        assertEquals(LocalDate.now().plusMonths(36), result.getEndDate());
    }
}
