package com.example.creditproducts.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MainController {
    @GetMapping(value = "/")
    public String getPage(){
        return "Главная страница проекта. Для просмотра документации перейдите на: /тут будет путь на документацию/";
    }
}
