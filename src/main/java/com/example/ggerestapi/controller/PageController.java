package com.example.ggerestapi.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.ggerestapi.entity.Emission;
import com.example.ggerestapi.entity.User;
import com.example.ggerestapi.repository.EmissionRepository;

import jakarta.servlet.http.HttpSession;

@Controller
public class PageController {
    @Autowired
    private EmissionRepository emissionRepository;

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    @GetMapping("/register")
    public String registerPage() {
        return "register";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate(); // Clear the session
        return "redirect:/login";
    }

    @GetMapping("/")
    public String indexPage() {
        return "redirect:/dashboard";
    }

    @GetMapping("/dashboard")
    public String dashboardPage(Model model, HttpSession session) {
        User authenticatedUser = (User) session.getAttribute("authenticatedUser");
        if (authenticatedUser == null) {
            return "redirect:/login";
        }
        return "dashboard";
    }

    @GetMapping("/profile")
    public String profilePage(Model model, HttpSession session) {
        User authenticatedUser = (User) session.getAttribute("authenticatedUser");
        if (authenticatedUser == null) {
            return "redirect:/login";
        }
        model.addAttribute("user", authenticatedUser);
        return "profile";
    }

    @GetMapping("/emissions")
    public String emissionsPage(Model model, HttpSession session) {
        User authenticatedUser = (User) session.getAttribute("authenticatedUser");
        if (authenticatedUser == null) {
            return "redirect:/login";
        }

        List<Emission> emissions = emissionRepository.findAll();
        List<String> categories = emissionRepository.findAllCategories();
        model.addAttribute("emissions", emissions);
        model.addAttribute("categories", categories);
        return "emissions";
    }
}
