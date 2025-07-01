package com.example.creditproducts.controller;

import com.example.creditproducts.dto.CreditProductDTO;
import com.example.creditproducts.model.CreditProduct;
import com.example.creditproducts.repository.CreditProductRepository;
import com.example.creditproducts.service.CreditProductService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.BindingResult;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class CreditProductControllerTest {

    private MockMvc mockMvc;
    private ObjectMapper objectMapper = new ObjectMapper();

    @Mock
    private CreditProductService creditProductService;

    @Mock
    private CreditProductRepository creditProductRepository;

    @InjectMocks
    private CreditProductController creditProductController;

    private CreditProduct testProduct;
    private CreditProductDTO testProductDTO;

    @BeforeEach
    void setUp() {
        creditProductController = new CreditProductController(creditProductService, creditProductRepository);

        mockMvc = MockMvcBuilders.standaloneSetup(creditProductController).build();

        testProduct = new CreditProduct();
        testProduct.setId(1L);
        testProduct.setName("Test Product");
        testProduct.setInterestRate(BigDecimal.valueOf(10.5));
        testProduct.setMinAmount(BigDecimal.valueOf(5000.0));
        testProduct.setMaxAmount(BigDecimal.valueOf(50000.0));
        testProduct.setTermMin(6);
        testProduct.setTermMax(36);

        testProductDTO = new CreditProductDTO();
        testProductDTO.setId(1L);
        testProductDTO.setName("Test Product");
        testProductDTO.setInterestRate(new BigDecimal("10.5"));
        testProductDTO.setMinAmount(new BigDecimal("5000.00"));
        testProductDTO.setMaxAmount(new BigDecimal("50000.00"));
        testProductDTO.setTermMin(6);
        testProductDTO.setTermMax(36);
    }

    @Test
    void getCreditProducts_ShouldReturnListOfProducts() throws Exception {

        List<CreditProductDTO> products = Collections.singletonList(testProductDTO);
        when(creditProductService.getAll()).thenReturn(products);


        mockMvc.perform(get("/credit-products"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].name", is(testProductDTO.getName())))
                .andExpect(jsonPath("$[0].interestRate", is(10.5)));

        verify(creditProductService).getAll();
    }

    @Test
    void getCreditProductById_WhenProductExists_ShouldReturnProduct() throws Exception {

        when(creditProductRepository.findById(1L)).thenReturn(Optional.of(testProduct));
        when(creditProductService.getById(1L)).thenReturn(testProductDTO);


        mockMvc.perform(get("/credit-products/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is(testProductDTO.getName())));

        verify(creditProductRepository).findById(1L);
        verify(creditProductService).getById(1L);
    }

    @Test
    void getCreditProductById_WhenProductNotExists_ShouldReturnNotFound() throws Exception {

        when(creditProductRepository.findById(1L)).thenReturn(Optional.empty());


        mockMvc.perform(get("/credit-products/1"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$").doesNotExist());

        verify(creditProductRepository).findById(1L);
        verify(creditProductService, never()).getById(anyLong());
    }

    @Test
    void createCreditProduct_WithValidData_ShouldReturnCreated() throws Exception {

        when(creditProductRepository.existsByName(testProduct.getName())).thenReturn(false);
        when(creditProductRepository.save(any(CreditProduct.class))).thenReturn(testProduct);


        mockMvc.perform(post("/credit-products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testProduct)))
                .andExpect(status().isCreated())
                .andExpect(content().string(containsString("Добавлен новый кредитный продукт с id: 1")));

        verify(creditProductRepository).existsByName(testProduct.getName());
        verify(creditProductRepository).save(any(CreditProduct.class));
    }

    @Test
    void createCreditProduct_WithDuplicateName_ShouldThrowException() throws Exception {

        when(creditProductRepository.existsByName(testProduct.getName())).thenReturn(true);


        mockMvc.perform(post("/credit-products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testProduct)))
                .andExpect(status().isConflict());

        verify(creditProductRepository).existsByName(testProduct.getName());
        verify(creditProductRepository, never()).save(any());
    }


    @Test
    void updateCreditProduct_WithValidData_ShouldReturnUpdated() throws Exception {

        when(creditProductRepository.findById(1L)).thenReturn(Optional.of(testProduct));

        mockMvc.perform(put("/credit-products/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testProductDTO)))
                .andExpect(status().isCreated())
                .andExpect(content().string(containsString("Условия обновлены")));

        verify(creditProductRepository).findById(1L);

    }

    @Test
    void updateCreditProduct_WhenProductNotExists_ShouldReturnNotFound() throws Exception {

        when(creditProductRepository.findById(1L)).thenReturn(Optional.empty());

        mockMvc.perform(put("/credit-products/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testProductDTO)))
                .andExpect(status().isNotFound());

        verify(creditProductRepository).findById(1L);
        verify(creditProductService, never()).update(any(), anyLong());
    }


}
