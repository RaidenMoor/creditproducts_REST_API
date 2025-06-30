package com.example.creditproducts.controller;

import com.example.creditproducts.dto.ClientDTO;
import com.example.creditproducts.dto.CreditApplicationDTO;
import com.example.creditproducts.exception.AccessException;
import com.example.creditproducts.exception.ApplicationNotFoundException;
import com.example.creditproducts.exception.ClientNotFoundException;
import com.example.creditproducts.exception.InvalidApplicationStatusException;
import com.example.creditproducts.model.Client;
import com.example.creditproducts.model.CreditApplication;
import com.example.creditproducts.model.CreditProduct;
import com.example.creditproducts.repository.ClientRepository;
import com.example.creditproducts.repository.CreditApplicationRepository;
import com.example.creditproducts.service.CreditApplicationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@RestController
@RequestMapping("/applications")
public class CreditApplicationController {


    CreditApplicationRepository creditApplicationRepository;
    CreditApplicationService creditApplicationService;

    @Autowired
    ClientRepository clientRepository;

    public CreditApplicationController(CreditApplicationService creditApplicationService){
        this.creditApplicationService = creditApplicationService;
    }

    @GetMapping
    @Operation(summary = "Просмотр заявок")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Список заявок предоставлен"),
            @ApiResponse(responseCode = "403", description = "Нет прав доступа")
    })
    public List<CreditApplicationDTO> getCreditApplications(){
        return creditApplicationService.getAll();

    }

    @GetMapping("/{id}")
    @Operation(summary = "Просмотреть кредитную заявку")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Заявка найдена"),
            @ApiResponse(responseCode = "403", description = "Нет прав доступа"),
            @ApiResponse(responseCode = "404", description = "Заявка не найдена")
    })
    public String getCreditApplication(@PathVariable Long id){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();


        Optional<CreditApplication> creditApplicationOptional = creditApplicationRepository.findById(id);
        if (creditApplicationOptional.isEmpty()) {
            throw new ApplicationNotFoundException(id);
        }

        CreditApplication creditApplication = creditApplicationOptional.get();

        // Проверка авторизации
        if (authentication != null && authentication.isAuthenticated()) {
            Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
            boolean isUser = authorities.stream()
                    .anyMatch(authority -> authority.getAuthority().equals("ROLE_USER"));

            if (isUser) {
                // Получаем имя пользователя из аутентификации
                String currentUsername = authentication.getName();

                // Проверяем, принадлежит ли заявка клиенту, связанному с текущим пользователем
                if (!isApplicationOwner(creditApplication, currentUsername)) {
                    throw new AccessException();
                }
            }
        }
        return creditApplicationService.getById(id).getStatus();

    }

    @PutMapping("/{id}/status")
    @Operation(summary = "Обновить статус заявки")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Статус обновлен"),
            @ApiResponse(responseCode = "403", description = "Нет прав доступа"),
            @ApiResponse(responseCode = "400", description = "Введен несуществующий статус"),
            @ApiResponse(responseCode = "404", description = "Заявка не найдена")
    })
    public ResponseEntity<?> updateStatus(@PathVariable Long id,@Valid @RequestBody String status,
                                             BindingResult bindingResult){
        if (bindingResult.hasErrors())
            return new ResponseEntity<>(bindingResult.getAllErrors(), HttpStatus.BAD_REQUEST);
        Optional<CreditApplication> creditApplication = creditApplicationRepository.findById(id);
        if (creditApplication.isEmpty()) {
            throw new ApplicationNotFoundException(id);
        }
        if (status.isEmpty() || !status.equals("PENDING") && !status.equals("APPROVED") && !status.equals("REJECTED")) {
            throw new InvalidApplicationStatusException(status);
        }

        creditApplicationService.updateStatus(id, status);
        return new ResponseEntity<>("Статус изменен. Новый статус заявки: " +  status, HttpStatus.OK);

    }


    @PostMapping
    @Operation(summary = "Добавить заявку")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Заявка создана"),
            @ApiResponse(responseCode = "400", description = "Некорректно введены данные")
    })
    public ResponseEntity<?> createApplication(@Valid @RequestBody CreditApplicationDTO creditApplicationDTO,
                                    BindingResult bindingResult){
        if (bindingResult.hasErrors()) {
            return new ResponseEntity<>(bindingResult.getAllErrors(), HttpStatus.BAD_REQUEST);
        }
        if(creditApplicationDTO.getStatus().isEmpty())
            throw new InvalidApplicationStatusException(creditApplicationDTO.getStatus());
        creditApplicationService.create(creditApplicationDTO);
        return new ResponseEntity<>("Новая заявка создана", HttpStatus.CREATED);

    }

    @GetMapping(value = "/client/{id}")
    @Operation(summary = "Просмотр заявок клиента")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Заявки клиента найдены"),
            @ApiResponse(responseCode = "403", description = "Нет прав доступа"),
            @ApiResponse(responseCode = "404", description = "Клиент не найден")
    })
    public List<CreditApplicationDTO> findByClient(@PathVariable Long id){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Optional<Client> clientOptional = clientRepository.findById(id);

        if (clientOptional.isEmpty()) {
            throw new ClientNotFoundException(id);
        }

        Client client = clientOptional.get();

        // Проверка авторизации
        if (authentication != null && authentication.isAuthenticated()) {
            if (authentication != null && authentication.isAuthenticated()) {
                Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
                boolean isUser = authorities.stream()
                        .anyMatch(authority -> authority.getAuthority().equals("ROLE_USER"));

                if (isUser) {
                    // Получаем имя пользователя из аутентификации
                    String currentUsername = authentication.getName();
                    if(client.getUser() != null) {

                        // Проверяем, является ли текущий пользователь владельцем клиента
                        if (!Objects.equals(client.getUser().getUsername(), currentUsername)) {
                            throw new AccessException();
                        }
                    }
                    else throw new AccessException();
                }
            }
        }

        return creditApplicationService.getAllByClientid(id);
    }

    @Autowired
    public void setCreditApplicationRepository(CreditApplicationRepository creditApplicationRepository){
        this.creditApplicationRepository = creditApplicationRepository;
    }

    // Вспомогательный метод для проверки, является ли текущий пользователь владельцем заявки
    private boolean isApplicationOwner(CreditApplication creditApplication, String currentUsername) {
        if (creditApplication.getClient() == null || creditApplication.getClient().getUser() == null) {
            return false; //  Если клиент или пользователь не связаны, считаем, что нет доступа
        }
        return Objects.equals(creditApplication.getClient().getUser().getUsername(), currentUsername);
    }


}
