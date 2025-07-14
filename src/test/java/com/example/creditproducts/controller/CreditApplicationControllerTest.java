package com.example.creditproducts.controller;

import com.example.creditproducts.dto.CreditApplicationDTO;
import com.example.creditproducts.exception.ApplicationNotFoundException;
import com.example.creditproducts.exception.ClientNotFoundException;
import com.example.creditproducts.exception.InvalidApplicationStatusException;
import com.example.creditproducts.model.ApplicationStatus;
import com.example.creditproducts.model.Client;
import com.example.creditproducts.model.CreditApplication;
import com.example.creditproducts.model.User;
import com.example.creditproducts.repository.ClientRepository;
import com.example.creditproducts.repository.CreditApplicationRepository;
import com.example.creditproducts.service.CreditApplication.CreditApplicationService;
import com.example.creditproducts.service.SecurityService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.BindingResult;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CreditApplicationControllerTest {

    private MockMvc mockMvc;

    @Mock
    private CreditApplicationService creditApplicationService;

    @Mock
    private CreditApplicationRepository creditApplicationRepository;

    @Mock
    private ClientRepository clientRepository;

    @InjectMocks
    private CreditApplicationController creditApplicationController;

    @Mock
    private Authentication authentication;

    @Mock
    private SecurityContext securityContext;

    @Mock
    private SecurityService securityService;


    @Mock
    private BindingResult bindingResult;


    private CreditApplicationDTO creditApplicationDTO;
    private CreditApplication creditApplication;
    private Client client;
    private User user;

    @BeforeEach
    void setUp() {
        creditApplicationController = new CreditApplicationController(creditApplicationRepository,
                creditApplicationService, securityService, clientRepository);


        mockMvc = MockMvcBuilders.standaloneSetup(creditApplicationController).build();

        creditApplicationDTO = new CreditApplicationDTO();
        creditApplicationDTO.setId(1L);
        creditApplicationDTO.setStatus("PENDING");

        creditApplication = new CreditApplication();
        creditApplication.setId(1L);

        user = new User();
        user.setUsername("testuser");

        client = new Client();
        client.setId(1L);
        client.setUser(user);
    }

    @Test
    void getCreditApplications_ReturnsListOfDTOs() {

        List<CreditApplicationDTO> dtoList = Arrays.asList(creditApplicationDTO, creditApplicationDTO);
        when(creditApplicationService.getAll()).thenReturn(dtoList);


        List<CreditApplicationDTO> result = creditApplicationController.getCreditApplications();


        assertNotNull(result);
        assertEquals(2, result.size());
        verify(creditApplicationService, times(1)).getAll();
    }

    @Test
    void getCreditApplication_ExistingIdAndAnonymousUser_ReturnsStatus() {

        when(creditApplicationRepository.findById(1L)).thenReturn(Optional.of(creditApplication));
        when(creditApplicationService.getById(1L)).thenReturn(creditApplicationDTO);
        creditApplicationDTO.setStatus(ApplicationStatus.APPROVED.toString());
        SecurityContextHolder.setContext(securityContext);
        when(securityContext.getAuthentication()).thenReturn(null);


        CreditApplicationDTO result = creditApplicationController.getCreditApplication(1L);


        assertNotNull(result);
        assertEquals(ApplicationStatus.APPROVED.toString(), result);
        verify(creditApplicationRepository, times(1)).findById(1L);
        verify(creditApplicationService, times(1)).getById(1L);
    }





    @Test
    void getCreditApplication_NonExistingId_ThrowsApplicationNotFoundException() {

        when(creditApplicationRepository.findById(1L)).thenReturn(Optional.empty());


        assertThrows(ApplicationNotFoundException.class, () -> creditApplicationController.getCreditApplication(1L));
        verify(creditApplicationService, never()).getById(anyLong());
    }

    @Test
    void updateStatus_ValidStatus_ReturnsOK() {

        String status = "APPROVED";
        when(creditApplicationRepository.findById(1L)).thenReturn(Optional.of(creditApplication));
        when(creditApplicationService.updateStatus(1L, status)).thenReturn(creditApplicationDTO);


        ResponseEntity<?> responseEntity = creditApplicationController.updateStatus(1L, status, bindingResult);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertTrue(responseEntity.getBody().toString().contains("Статус изменен"));
        verify(creditApplicationService, times(1)).updateStatus(1L, status);
    }

    @Test
    void updateStatus_InvalidStatus_ThrowsInvalidApplicationStatusException() {

        String status = "INVALID";
        when(creditApplicationRepository.findById(1L)).thenReturn(Optional.of(creditApplication));


        assertThrows(InvalidApplicationStatusException.class, () -> creditApplicationController.updateStatus(1L, status, bindingResult));
        verify(creditApplicationService, never()).updateStatus(anyLong(), anyString());
    }

    @Test
    void updateStatus_NonExistingApplicationId_ThrowsApplicationNotFoundException() {

        String status = "APPROVED";
        when(creditApplicationRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ApplicationNotFoundException.class, () -> creditApplicationController.updateStatus(1L, status, bindingResult));
        verify(creditApplicationService, never()).updateStatus(anyLong(), anyString());
    }

    @Test
    void createApplication_ValidInput_ReturnsCreated() {

        when(bindingResult.hasErrors()).thenReturn(false);
        when(creditApplicationService.create(creditApplicationDTO)).thenReturn(creditApplicationDTO);


        ResponseEntity<?> response = creditApplicationController.createApplication(creditApplicationDTO, bindingResult);


        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("Новая заявка создана", response.getBody());
        verify(creditApplicationService, times(1)).create(creditApplicationDTO);
    }



    @Test
    void createApplication_EmptyStatus_ThrowsInvalidApplicationStatusException() {
        // Arrange
        creditApplicationDTO.setStatus("");
        when(bindingResult.hasErrors()).thenReturn(false);

        // Act & Assert
        assertThrows(InvalidApplicationStatusException.class, () -> creditApplicationController.createApplication(creditApplicationDTO, bindingResult));
        verify(creditApplicationService, never()).create(any());
    }






    @Test
    void findByClient_ExistingClientAndNoUserLoggedIn_ReturnsListDTOs() {
        // Arrange
        when(clientRepository.findById(1L)).thenReturn(Optional.of(client));
        SecurityContextHolder.setContext(securityContext);
        when(securityContext.getAuthentication()).thenReturn(null);

        List<CreditApplicationDTO> dtoList = Arrays.asList(creditApplicationDTO, creditApplicationDTO);
        when(creditApplicationService.getAllByClientid(1L)).thenReturn(dtoList);

        // Act
        List<CreditApplicationDTO> result = creditApplicationController.findByClient(1L);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        verify(creditApplicationService, times(1)).getAllByClientid(1L);
    }


    @Test
    void findByClient_NonExistingClient_ThrowsClientNotFoundException() {

        when(clientRepository.findById(1L)).thenReturn(Optional.empty());


        assertThrows(ClientNotFoundException.class, () -> creditApplicationController.findByClient(1L));
        verify(creditApplicationService, never()).getAllByClientid(anyLong());
    }
}