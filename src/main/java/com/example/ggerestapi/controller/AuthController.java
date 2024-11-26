package com.example.ggerestapi.controller;

import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.ggerestapi.entity.User;
import com.example.ggerestapi.repository.UserRepository;

@Controller
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/register")
    public String register(@RequestParam String email, @RequestParam String name, @RequestParam String password,
            HttpSession session, RedirectAttributes redirectAttributes) {
        User existingUser = userRepository.findByEmail(email);
        if (existingUser != null) {
            redirectAttributes.addFlashAttribute("error", "User already exists.");
            return "redirect:/login";
        }
        User user = new User();
        user.setEmail(email);
        user.setName(name);
        user.setPassword(password);
        userRepository.save(user);
        User authenticatedUser = userRepository.findByEmail(email);
        session.setAttribute("authenticatedUser", authenticatedUser);
        return "redirect:/dashboard";
    }

    @PostMapping("/login")
    public String login(@RequestParam String email, @RequestParam String password, RedirectAttributes attributes, HttpSession session) {
        if (email == null || password == null) {
            attributes.addFlashAttribute("error", "Email or password is null.");
            return "redirect:/login";
        }
        User user = userRepository.findByEmail(email);
        if (user == null) {
            attributes.addFlashAttribute("error", "User not found.");
            return "redirect:/login";
        }
        if (!user.getPassword().equals(password)) {
            attributes.addFlashAttribute("error", "Password is incorrect.");
            return "redirect:/login";
        }
        User authenticatedUser = userRepository.findByEmail(email);
        session.setAttribute("authenticatedUser", authenticatedUser);
        return "redirect:/dashboard";
    }
}