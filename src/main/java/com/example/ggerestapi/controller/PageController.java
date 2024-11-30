package com.example.ggerestapi.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.ggerestapi.entity.Emission;
import com.example.ggerestapi.entity.User;
import com.example.ggerestapi.repository.EmissionRepository;
import com.example.ggerestapi.repository.UserRepository;

import jakarta.servlet.http.HttpSession;

@Controller
public class PageController {
    @Autowired
    private EmissionRepository emissionRepository;

    @Autowired
    private UserRepository userRepository;

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
        session.invalidate();
        return "redirect:/login";
    }

    @GetMapping("/")
    public String indexPage() {
        return "redirect:/dashboard";
    }

    @GetMapping("/dashboard")
    public String dashboardPage(RedirectAttributes redirectAttributes, HttpSession session) {
        User authenticatedUser = (User) session.getAttribute("authenticatedUser");
        if (authenticatedUser == null) {
            redirectAttributes.addFlashAttribute("error", "Logged out of session.");
            return "redirect:/login";
        }
        return "dashboard";
    }

    @GetMapping("/profile")
    public String profilePage(HttpSession session, RedirectAttributes redirectAttributes, Model model) {
        User authenticatedUser = (User) session.getAttribute("authenticatedUser");
        if (authenticatedUser == null) {
            redirectAttributes.addFlashAttribute("error", "Logged out of your session.");
            return "redirect:/login";
        }
        model.addAttribute("authenticatedUser", authenticatedUser);
        List<User> users = userRepository.findAll();
        model.addAttribute("users", users);
        return "profile";
    }

    @GetMapping("/emissions")
    public String emissionsPage(Model model, HttpSession session, RedirectAttributes redirectAttributes) {
        User authenticatedUser = (User) session.getAttribute("authenticatedUser");
        if (authenticatedUser == null) {
            redirectAttributes.addFlashAttribute("error", "Logged out of session.");
            return "redirect:/login";
        }

        List<Emission> emissions = emissionRepository.findAll();
        List<String> categories = emissionRepository.findAllCategories();
        model.addAttribute("emissions", emissions);
        model.addAttribute("categories", categories);
        return "emissions";
    }

    @GetMapping("/update-emission/{id}")
    public String updateEmissionPage(Model model, HttpSession session, @PathVariable Long id,
            RedirectAttributes redirect) {
        User authenticatedUser = (User) session.getAttribute("authenticatedUser");
        if (authenticatedUser == null) {
            return "redirect:/login";
        }
        if (id == null) {
            redirect.addFlashAttribute("error", "id parameter is required.");
            return "redirect:/emissions";
        }
        Emission emission = emissionRepository.findById(id).orElse(null);
        if (emission == null) {
            redirect.addFlashAttribute("error", "Emission to edit not found.");
            return "redirect:/emissions";
        }
        model.addAttribute("emission", emission);
        return "update-emission";
    }

    @GetMapping("/add-emission")
    public String addEmissionPage(HttpSession session, RedirectAttributes redirectAttributes) {
        User authenticatedUser = (User) session.getAttribute("authenticatedUser");
        if (authenticatedUser == null) {
            redirectAttributes.addFlashAttribute("error", "Logged out of session.");
            return "redirect:/login";
        }
        return "add-emission";
    }
}
