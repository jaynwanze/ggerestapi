package com.example.ggerestapi.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PageController {

    @GetMapping("/login")
    public String loginPage() {
        return "login.html"; // Renders src/main/resources/templates/login.html
    }

    @GetMapping("/register")
    public String registerPage() {
        return "register"; // Renders src/main/resources/templates/register.html
    }
}
