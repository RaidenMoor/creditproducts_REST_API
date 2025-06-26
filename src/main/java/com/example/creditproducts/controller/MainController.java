package com.example.creditproducts.controller;

import com.example.creditproducts.dto.IssuedLoanDTO;
import com.example.creditproducts.model.IssuedLoan;
import com.example.creditproducts.service.IssuedLoanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class MainController {

    @Autowired
    IssuedLoanService issuedLoanService;
    @GetMapping(value = "/")
    public String getPage(){
        return "Главная страница проекта. Для просмотра документации перейдите на: /тут будет путь на документацию/";
    }

    @GetMapping(value = "/issued-loans")
    public List<IssuedLoanDTO> getIssuedLoan (){
        return issuedLoanService.getAll();
    }
}
