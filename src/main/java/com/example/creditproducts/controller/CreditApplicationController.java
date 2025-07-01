package com.example.creditproducts.controller;

import com.example.creditproducts.dto.CreditApplicationDTO;
import com.example.creditproducts.exception.AccessException;
import com.example.creditproducts.exception.ApplicationNotFoundException;
import com.example.creditproducts.exception.ClientNotFoundException;
import com.example.creditproducts.exception.InvalidApplicationStatusException;
import com.example.creditproducts.model.Client;
import com.example.creditproducts.model.CreditApplication;
import com.example.creditproducts.repository.ClientRepository;
import com.example.creditproducts.repository.CreditApplicationRepository;
import com.example.creditproducts.service.CreditApplication.CreditApplicationService;
import com.example.creditproducts.service.SecurityService;
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


    private final CreditApplicationRepository creditApplicationRepository;
    private final CreditApplicationService creditApplicationService;
    private final SecurityService securityService;
    private final ClientRepository clientRepository;

    @Autowired
    public CreditApplicationController(CreditApplicationRepository creditApplicationRepository,
                                       CreditApplicationService creditApplicationService,
                                       SecurityService securityService, ClientRepository clientRepository){
        this.creditApplicationService = creditApplicationService;
        this.creditApplicationRepository = creditApplicationRepository;
        this.securityService = securityService;
        this.clientRepository = clientRepository;
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
    public CreditApplicationDTO getCreditApplication(@PathVariable Long id){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

       creditApplicationRepository.findById(id).orElseThrow(()-> new ApplicationNotFoundException(id));

        securityService.userRoleCheck(authentication);

        return creditApplicationService.getById(id);

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

        clientRepository.findById(id).orElseThrow(()->
                new ClientNotFoundException(id));

        securityService.userRoleCheck(authentication, id);

        return creditApplicationService.getAllByClientid(id);
    }



}
