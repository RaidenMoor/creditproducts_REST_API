package com.example.creditproducts.controller;

import com.example.creditproducts.dto.CreditProductDTO;
import com.example.creditproducts.model.CreditProduct;
import com.example.creditproducts.repository.CreditProductRepository;
import com.example.creditproducts.repository.UserRepository;
import com.example.creditproducts.service.CreditProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
        return creditProductService.getById(id);

    }

    @PostMapping
    public String createCreditProduct(@RequestBody CreditProduct creditProduct){
        creditProductRepository.save(creditProduct);
        return "Добавлен новый кредитный продукт";

    }

    @PutMapping(value = "/{id}")
    public String updateCreditProduct(@PathVariable Long id, @RequestBody CreditProductDTO creditProductDTO){
        creditProductService.update(creditProductDTO,id);
        return "Условия обновлены";
    }

    @Autowired
    public void setCreditProductRepository(CreditProductRepository creditProductRepository){
        this.creditProductRepository = creditProductRepository;
    }
}
