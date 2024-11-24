package com.example.ggerestapi.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.example.ggerestapi.entity.User;
import com.example.ggerestapi.repository.UserRepository;

@Controller
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/register")
    public String register(@RequestParam String email, @RequestParam String name, @RequestParam String password,
            HttpSession session) {
        User user = new User();
        user.setEmail(email);
        user.setName(name);
        user.setPassword(password);
        userRepository.save(user);
        session.setAttribute("authenticatedUser", user);
        return "redirect:/dashboard";
    }

    @PostMapping("/login")
    public String login(@RequestParam String email, @RequestParam String password, Model model, HttpSession session) {
        if (email == null || password == null) {
            model.addAttribute("error", "Email or password is null.");
            return "redirect:/login";
        }
        User user = userRepository.findByEmail(email);
        if (user == null) {
            model.addAttribute("error", "User not found.");
            return "redirect:/login";
        }
        if (!user.getPassword().equals(password)) {
            model.addAttribute("error", "Password is incorrect.");
            return "redirect:/login";
        }
        session.setAttribute("authenticatedUser", user);
        return "redirect:/dashboard";
    }
}