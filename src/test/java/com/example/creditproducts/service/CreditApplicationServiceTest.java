package com.example.creditproducts.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.example.creditproducts.dto.CreditApplicationDTO;
import com.example.creditproducts.dto.CreditProductDTO;
import com.example.creditproducts.dto.IssuedLoanDTO;
import com.example.creditproducts.exception.InvalidAmountException;
import com.example.creditproducts.exception.InvalidTermMonthsException;
import com.example.creditproducts.mapper.CreditApplicationMapper;
import com.example.creditproducts.model.ApplicationStatus;
import com.example.creditproducts.model.CreditApplication;
import com.example.creditproducts.model.CreditProduct;
import com.example.creditproducts.repository.CreditApplicationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class CreditApplicationServiceTest {

    @Mock
    private CreditApplicationRepository creditApplicationRepository;

    @Mock
    private CreditApplicationMapper creditApplicationMapper;

    @Mock
    private IssuedLoanService issuedLoanService;

    @Mock
    private CreditProductService creditProductService;

    @InjectMocks
    private CreditApplicationService creditApplicationService;

    private CreditApplicationDTO testDto;
    private CreditApplication testEntity;
    private CreditProductDTO testCreditProductDto;

    @BeforeEach
    void setUp() {
        creditApplicationService.setCreditProductService(creditProductService);
        creditApplicationService.setIssuedLoanService(issuedLoanService);
        testDto = new CreditApplicationDTO();
        testDto.setId(1L);
        testDto.setAmount(BigDecimal.valueOf(10000.0));
        testDto.setTermMonths(12);
        testDto.setCreditProductId(1L);
        testDto.setStatus("PENDING");

        testCreditProductDto = new CreditProductDTO();
        testCreditProductDto.setId(1L);
        testCreditProductDto.setMinAmount(BigDecimal.valueOf(5000.0));
        testCreditProductDto.setMaxAmount(BigDecimal.valueOf(20000.0));
        testCreditProductDto.setTermMin(6);
        testCreditProductDto.setTermMax(24);

        CreditProduct testCreditProduct = new CreditProduct();
        testCreditProduct.setId(1L);
        testCreditProduct.setMinAmount(BigDecimal.valueOf(5000.0));
        testCreditProduct.setMaxAmount(BigDecimal.valueOf(20000.0));
        testCreditProduct.setTermMin(6);
        testCreditProduct.setTermMax(24);

        testEntity = new CreditApplication();
        testEntity.setId(1L);
        testEntity.setAmount(BigDecimal.valueOf(10000.0));
        testEntity.setTermMonths(12);
        testEntity.setCreditProduct(testCreditProduct);
        testEntity.setStatus(ApplicationStatus.PENDING);


    }

    @Test
    void getAllByClientId_ShouldReturnListOfApplications() {

        Long clientId = 1L;
        when(creditApplicationRepository.findByClientId(clientId))
                .thenReturn(Collections.singletonList(testEntity));
        when(creditApplicationMapper.toDTOs(anyList()))
                .thenReturn(Collections.singletonList(testDto));


        List<CreditApplicationDTO> result = creditApplicationService.getAllByClientid(clientId);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(testDto, result.get(0));
        verify(creditApplicationRepository).findByClientId(clientId);
    }

    @Test
    void updateStatus_WhenApplicationExists_ShouldUpdateStatus() {

        Long id = 1L;
        String newStatus = "APPROVED";
        when(creditApplicationRepository.findById(id)).thenReturn(Optional.of(testEntity));
        when(creditApplicationRepository.save(any())).thenReturn(testEntity);
        when(creditApplicationMapper.toDTO(testEntity)).thenReturn(testDto);


        CreditApplicationDTO result = creditApplicationService.updateStatus(id, newStatus, issuedLoanService);

        assertNotNull(result);
        assertEquals(testDto, result);
        verify(creditApplicationRepository).findById(id);
        verify(creditApplicationRepository).save(testEntity);
    }

    @Test
    void updateStatus_WhenApplicationNotExists_ShouldReturnNull() {

        Long id = 99L;
        String newStatus = "APPROVED";
        when(creditApplicationRepository.findById(id)).thenReturn(Optional.empty());


        CreditApplicationDTO result = creditApplicationService.updateStatus(id, newStatus);


        assertNull(result);
        verify(creditApplicationRepository).findById(id);
        verify(creditApplicationRepository, never()).save(any());
    }

    @Test
    void updateStatus_WhenStatusApproved_ShouldCreateLoan() {

        Long id = 1L;
        String newStatus = "APPROVED";
        when(creditApplicationRepository.findById(id)).thenReturn(Optional.of(testEntity));
        when(creditApplicationRepository.save(any())).thenReturn(testEntity);
        when(creditApplicationMapper.toDTO(testEntity)).thenReturn(testDto);
        when(issuedLoanService.createLoan(testDto)).thenReturn(new IssuedLoanDTO());

        CreditApplicationDTO result = creditApplicationService.updateStatus(id, newStatus, issuedLoanService);


        assertNotNull(result);
        verify(issuedLoanService).createLoan(testDto);
    }

    @Test
    void create_WithValidData_ShouldReturnCreatedApplication() {

        when(creditApplicationMapper.toEntity(testDto)).thenReturn(testEntity);
        when(creditApplicationRepository.existsById(testEntity.getId())).thenReturn(false);
        when(creditApplicationRepository.save(testEntity)).thenReturn(testEntity);
        when(creditApplicationMapper.toDTO(testEntity)).thenReturn(testDto);


        CreditApplicationDTO result = creditApplicationService.create(testDto, testCreditProductDto);


        assertNotNull(result);
        assertEquals(testDto, result);
        verify(creditApplicationRepository).save(testEntity);
    }

    @Test
    void create_WithNullInput_ShouldReturnNull() {

        CreditApplicationDTO result = creditApplicationService.create(null);


        assertNull(result);
    }

    @Test
    void create_WithExistingId_ShouldReturnNull() {

        when(creditApplicationMapper.toEntity(testDto)).thenReturn(testEntity);
        when(creditApplicationRepository.existsById(testEntity.getId())).thenReturn(true);

        CreditApplicationDTO result = creditApplicationService.create(testDto, testCreditProductDto);

        assertNull(result);
        verify(creditApplicationRepository, never()).save(any());
    }

    @Test
    void create_WithAmountBelowMin_ShouldThrowException() {

        testDto.setAmount(BigDecimal.valueOf(4000.0));

        assertThrows(InvalidAmountException.class, () -> {
            creditApplicationService.create(testDto, testCreditProductDto);
        });
    }

    @Test
    void create_WithAmountAboveMax_ShouldThrowException() {

        testDto.setAmount(BigDecimal.valueOf(30000.0));

        assertThrows(InvalidAmountException.class, () -> {
            creditApplicationService.create(testDto, testCreditProductDto);
        });
    }

    @Test
    void create_WithTermBelowMin_ShouldThrowException() {

        testDto.setTermMonths(5);


        assertThrows(InvalidTermMonthsException.class, () -> {
            creditApplicationService.create(testDto, testCreditProductDto);
        });
    }

    @Test
    void create_WithTermAboveMax_ShouldThrowException() {

        testDto.setTermMonths(25);


        assertThrows(InvalidTermMonthsException.class, () -> {
            creditApplicationService.create(testDto, testCreditProductDto);
        });
    }
}