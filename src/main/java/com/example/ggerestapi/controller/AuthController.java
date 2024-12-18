package com.example.ggerestapi.controller;

import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.ggerestapi.entity.User;
import com.example.ggerestapi.service.AuthService;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/register")
    public String register(@RequestParam String email, @RequestParam String name, @RequestParam String password,
            HttpSession session, RedirectAttributes redirectAttributes) {
        return authService.register(email, name, password, session, redirectAttributes);
    }

    @PostMapping("/login")
    public String login(@RequestParam String email, @RequestParam String password, RedirectAttributes attributes,
            HttpSession session) {
        return authService.login(email, password, attributes, session);
    }

    @PostMapping("/json/login")
    @ResponseBody
    public String login(String email, String password, HttpSession session) {
        return authService.jsonLogin(email,password,  session);
    }

    @PostMapping("/json/register")
    @ResponseBody
    public String register(@PathVariable User user, HttpSession session) {
        return authService.jsonRegister(user, session);
    }

    @PostMapping("/json/logout")
    @ResponseBody
    public String logout(HttpSession session) {
        return authService.jsonLogout(session);
    }
}