package com.example.ggerestapi.service;

import com.example.ggerestapi.entity.User;
import com.example.ggerestapi.repository.UserRepository;

import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    public String register(String email, String name, String password, HttpSession session, RedirectAttributes redirectAttributes) {
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

    public String login(String email, String password, RedirectAttributes attributes, HttpSession session) {
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