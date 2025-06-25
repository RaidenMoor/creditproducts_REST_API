package com.example.creditproducts.controller;

import com.example.creditproducts.dto.ClientDTO;
import com.example.creditproducts.dto.CreditApplicationDTO;
import com.example.creditproducts.model.CreditApplication;
import com.example.creditproducts.model.CreditProduct;
import com.example.creditproducts.repository.CreditApplicationRepository;
import com.example.creditproducts.service.CreditApplicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/applications")
public class CreditApplicationController {

    CreditApplicationRepository creditApplicationRepository;
    CreditApplicationService creditApplicationService;

    public CreditApplicationController(CreditApplicationService creditApplicationService){
        this.creditApplicationService = creditApplicationService;
    }

    @GetMapping
    public List<CreditApplicationDTO> getCreditApplications(){
        return creditApplicationService.getAll();

    }

    @GetMapping("/{id}")
    public String getCreditApplication(@PathVariable Long id){
        return creditApplicationService.getById(id).getStatus();

    }

    @PutMapping("/{id}/status")
    public CreditApplicationDTO updateStatus(@PathVariable Long id, @RequestBody String status){
        return creditApplicationService.updateStatus(id, status);

    }


    @PostMapping
    public String createApplication(@RequestBody CreditApplicationDTO creditApplicationDTO){
        creditApplicationService.create(creditApplicationDTO);
        return "Создана кредитная заявка";

    }

    @GetMapping(value = "/client/{id}")
    public List<CreditApplicationDTO> findByClient(@PathVariable Long id){
        return creditApplicationService.getAllByClientid(id);
    }

    @Autowired
    public void setCreditApplicationRepository(CreditApplicationRepository creditApplicationRepository){
        this.creditApplicationRepository = creditApplicationRepository;
    }


}
