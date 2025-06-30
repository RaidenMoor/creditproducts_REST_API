package com.example.creditproducts.controller;

import com.example.creditproducts.dto.CreditProductDTO;
import com.example.creditproducts.exception.ClientNotFoundException;
import com.example.creditproducts.exception.CreditProductNotFoundException;
import com.example.creditproducts.exception.DublicateException;
import com.example.creditproducts.model.CreditProduct;
import com.example.creditproducts.repository.CreditProductRepository;
import com.example.creditproducts.service.CreditProductService;
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

    public CreditProductController (CreditProductService creditProductService){
        this.creditProductService = creditProductService;

    }

    @GetMapping
    public List<CreditProductDTO> getCreditProducts(){
        return creditProductService.getAll();

    }

    @GetMapping(value = "/{id}")
    public CreditProductDTO getCreditProducts(@PathVariable Long id){

        Optional<CreditProduct> creditProduct = creditProductRepository.findById(id);
        if (creditProduct.isEmpty()) {
            throw new CreditProductNotFoundException(id);
        }
        return creditProductService.getById(id);

    }

    @PostMapping
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
    public ResponseEntity<?> updateCreditProduct(@PathVariable Long id,
                                                 @Valid @RequestBody CreditProductDTO creditProductDTO,
                                                 BindingResult bindingResult){
        if(bindingResult.hasErrors())
            return new ResponseEntity<>(bindingResult.getAllErrors(), HttpStatus.BAD_REQUEST);

        Optional<CreditProduct> creditProduct = creditProductRepository.findById(id);
        if (creditProduct.isEmpty()) {
            throw new CreditProductNotFoundException(id);
        }

        creditProductService.update(creditProductDTO,id);
        return new ResponseEntity<>("Условия обновлены", HttpStatus.CREATED);
    }



    @Autowired
    public void setCreditProductRepository(CreditProductRepository creditProductRepository){
        this.creditProductRepository = creditProductRepository;
    }
}
