package com.example.creditproducts.controller;

import com.example.creditproducts.dto.CreditProductDTO;
import com.example.creditproducts.exception.ClientNotFoundException;
import com.example.creditproducts.exception.CreditProductNotFoundException;
import com.example.creditproducts.exception.DublicateException;
import com.example.creditproducts.model.CreditProduct;
import com.example.creditproducts.repository.CreditProductRepository;
import com.example.creditproducts.service.CreditProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/credit-products")
public class CreditProductController {

    CreditProductRepository creditProductRepository;
    CreditProductService creditProductService;

    @Autowired
    public CreditProductController (CreditProductService creditProductService,
                                    CreditProductRepository creditProductRepository){
        this.creditProductService = creditProductService;
        this.creditProductRepository = creditProductRepository;

    }

    @GetMapping
    @Operation(summary = "Просмотреть все кредитные продукты")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Список кредитный продуктов предоставлен")
    })
    public List<CreditProductDTO> getCreditProducts(){
        return creditProductService.getAll();

    }

    @GetMapping(value = "/{id}")
    @Operation(summary = "Просмотр кредитного продукта")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Кредитный пролукт найден"),
            @ApiResponse(responseCode = "403", description = "Нет прав доступа"),
            @ApiResponse(responseCode = "404", description = "Кредитный продукт не найден")
    })
    public CreditProductDTO getCreditProducts(@PathVariable Long id){

        creditProductRepository.findById(id).
                orElseThrow(() -> new CreditProductNotFoundException(id));

        return creditProductService.getById(id);

    }

    @PostMapping
    @Operation(summary = "Добавить кредитный продукт")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Кредитный продукт добавлен"),
            @ApiResponse(responseCode = "403", description = "Нет прав доступа"),
            @ApiResponse(responseCode = "400", description = "Некорректно введены данные"),
            @ApiResponse(responseCode = "409", description = "Попытка добавления дублирующей записи")
    })
    public ResponseEntity<?> createCreditProduct(@Valid @RequestBody CreditProduct creditProduct,
                                      BindingResult bindingResult){
        if(bindingResult.hasErrors())
            return new ResponseEntity<>(bindingResult.getAllErrors(), HttpStatus.BAD_REQUEST);

        if(creditProductRepository.existsByName(creditProduct.getName()))
            throw new DublicateException("Кредитный продукт с таким названием уже существует.");

        CreditProduct savedProduct = creditProductRepository.save(creditProduct);
        return new ResponseEntity<>("Добавлен новый кредитный продукт с id: " + savedProduct.getId(),
                HttpStatus.CREATED);

    }

    @PutMapping(value = "/{id}")
    @Operation(summary = "Изменить условия кредитного продукта")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Условия обновлены"),
            @ApiResponse(responseCode = "403", description = "Нет прав доступа"),
            @ApiResponse(responseCode = "400", description = "Некорректно введены данные"),
            @ApiResponse(responseCode = "409", description = "Попытка добавления дублирующей записи")
    })
    public ResponseEntity<?> updateCreditProduct(@PathVariable Long id,
                                                 @Valid @RequestBody CreditProductDTO creditProductDTO,
                                                 BindingResult bindingResult){
        if(bindingResult.hasErrors())
            return new ResponseEntity<>(bindingResult.getAllErrors(), HttpStatus.BAD_REQUEST);

        creditProductRepository.findById(id).
                orElseThrow(() -> new CreditProductNotFoundException(id));

        creditProductService.update(creditProductDTO,id);
        return new ResponseEntity<>("Условия обновлены", HttpStatus.CREATED);
    }
}
