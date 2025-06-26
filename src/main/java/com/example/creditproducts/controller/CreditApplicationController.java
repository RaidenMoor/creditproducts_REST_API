package com.example.creditproducts.controller;

import com.example.creditproducts.dto.ClientDTO;
import com.example.creditproducts.dto.CreditApplicationDTO;
import com.example.creditproducts.exception.ApplicationNotFoundException;
import com.example.creditproducts.exception.ClientNotFoundException;
import com.example.creditproducts.exception.InvalidApplicationStatusException;
import com.example.creditproducts.model.Client;
import com.example.creditproducts.model.CreditApplication;
import com.example.creditproducts.model.CreditProduct;
import com.example.creditproducts.repository.ClientRepository;
import com.example.creditproducts.repository.CreditApplicationRepository;
import com.example.creditproducts.service.CreditApplicationService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
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
    public List<CreditApplicationDTO> getCreditApplications(){
        return creditApplicationService.getAll();

    }

    @GetMapping("/{id}")
    public String getCreditApplication(@PathVariable Long id){
        Optional<CreditApplication> creditProduct = creditApplicationRepository.findById(id);
        if (creditProduct.isEmpty()) {
            throw new ApplicationNotFoundException(id);
        }
        return creditApplicationService.getById(id).getStatus();

    }

    @PutMapping("/{id}/status")
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
    public List<CreditApplicationDTO> findByClient(@PathVariable Long id){
        Optional<Client> client = clientRepository.findById(id);
        if (client.isEmpty()) {
            throw new ClientNotFoundException(id);
        }
        return creditApplicationService.getAllByClientid(id);
    }

    @Autowired
    public void setCreditApplicationRepository(CreditApplicationRepository creditApplicationRepository){
        this.creditApplicationRepository = creditApplicationRepository;
    }


}
