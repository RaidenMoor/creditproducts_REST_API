package com.example.creditproducts.controller;

import com.example.creditproducts.dto.ClientDTO;
import com.example.creditproducts.exception.DublicateException;
import com.example.creditproducts.repository.ClientRepository;
import com.example.creditproducts.service.ClientService;
import com.example.creditproducts.service.SecurityService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/clients")
public class ClientController {

    private final ClientRepository clientRepository;
    private final ClientService clientService;
    private final SecurityService securityService;

    @Autowired
    public ClientController(ClientService clientService, ClientRepository clientRepository,
                            SecurityService securityService){

        this.clientService = clientService;
        this.securityService=securityService;
        this.clientRepository = clientRepository;
    }

    @GetMapping
    @Operation(summary = "Просмотр клиентов")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Список клиентов предоставлен"),
            @ApiResponse(responseCode = "403", description = "Нет прав доступа"),
    })
    public List<ClientDTO> getAllClients(){
        return clientService.getAll();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Просмотреть данные клиента")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Клиент найден"),
            @ApiResponse(responseCode = "403", description = "Нет прав доступа"),
            @ApiResponse(responseCode = "404", description = "Клиент не найден")
    })
    public ClientDTO getClient(@PathVariable Long id){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        securityService.userRoleCheck(authentication,id);

        return clientService.getById(id);

    }

    @PostMapping
    @Operation(summary = "Добавить клиента")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Клиент добавлен"),
            @ApiResponse(responseCode = "403", description = "Нет прав доступа"),
            @ApiResponse(responseCode = "400", description = "Некорректно введены данные"),
            @ApiResponse(responseCode = "409", description = "Попытка добавления дублирующей записи")
    })
    public ResponseEntity<?> createClient(@Valid @RequestBody ClientDTO clientDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            // Обработка ошибок валидации
            return new ResponseEntity<>(bindingResult.getAllErrors(), HttpStatus.BAD_REQUEST);
        }
        if(clientRepository.existsByEmail(clientDTO.getEmail())){
            throw new DublicateException("Пользователь с таким email уже существует");
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        Long userId = securityService.userRoleCheck(authentication);
        if(userId != 0L) clientDTO.setUserId(userId);

        ClientDTO savedClient = clientService.create(clientDTO);


        return new ResponseEntity<>("Добавлен новый клиент с id: " + savedClient.getId(), HttpStatus.CREATED); // 201 Created

    }

    @PutMapping("/update/{id}")
    @Operation(summary = "Обновить данные клиента")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Данные обновлены"),
            @ApiResponse(responseCode = "403", description = "Нет прав доступа"),
            @ApiResponse(responseCode = "400", description = "Некорректно введены данные"),
            @ApiResponse(responseCode = "404", description = "Клиент не найден")
    })
    public ResponseEntity<?> updateClient(@PathVariable Long id,@Valid @RequestBody ClientDTO clientDTO,
                               BindingResult bindingResult){
        if (bindingResult.hasErrors()) {
            return new ResponseEntity<>(bindingResult.getAllErrors(), HttpStatus.BAD_REQUEST);
        }
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        securityService.userRoleCheck(authentication, id);
        clientService.update(clientDTO, id);

        return new ResponseEntity<>("Обновлены данные клиента с id: " + id, HttpStatus.OK);



    }



}
