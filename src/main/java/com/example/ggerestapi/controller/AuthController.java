package com.example.ggerestapi.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.example.ggerestapi.entity.User;
import com.example.ggerestapi.repository.UserRepository;

@Controller
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/register")
    public String register(@RequestParam String email, @RequestParam String password, Model model) {
        User user = new User();
        user.setEmail(email);
        user.setPassword(password);
        userRepository.save(user);
        model.addAttribute("user", user);
        return "dashboard";
    }

    @PostMapping("/login")
    public String login(@RequestParam String email, @RequestParam String password, Model model) {
        if (email == null || password == null) {
            model.addAttribute("error", "Email or password is null.");
            return "login";
        }
        User user = userRepository.findByEmail(email);
        if (user == null) {
            model.addAttribute("error", "User not found.");
            return "login";
        }
        if (!user.getPassword().equals(password)) {
            model.addAttribute("error", "Password is incorrect.");
            return "login";
        }
        model.addAttribute("user", user);
        return "dashboard";
    }
}