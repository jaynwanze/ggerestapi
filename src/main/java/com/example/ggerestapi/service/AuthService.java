package com.example.ggerestapi.service;

import com.example.ggerestapi.entity.User;
import com.example.ggerestapi.repository.UserRepository;

import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    public String register(String email, String name, String password, HttpSession session,
            RedirectAttributes redirectAttributes) {
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

    public String jsonLogin(String email, String password, HttpSession session) {
        User existingUser = userRepository.findByEmail(email);
        if (existingUser == null) {
            return "User not found";
        }
        if (!existingUser.getPassword().equals(password)) {
            return "Password is incorrect";
        }
        if (email == null || password == null) {
            return "Email or password is null";
        }
        User currentUser = (User) session.getAttribute("authenticatedUser");

        if (currentUser != null && existingUser.getId() == currentUser.getId()) {
            return "User is already logged in";
        }
        session.setAttribute("authenticatedUser", existingUser);
        return "User authenticated and now logged in";
    }

    public String jsonRegister(@RequestBody User user, HttpSession session) {
        User existingUser = userRepository.findByEmail(user.getEmail());
        if (existingUser != null) {
            return "User already exists";
        }
        if (user.getEmail() == null || user.getName() == null || user.getPassword() == null) {
            return "User information is incomplete";
        }
        session.setAttribute("authenticatedUser", existingUser);
        userRepository.save(user);
        return "User created successfully and now logged in";
    }

    public String jsonLogout(HttpSession session) {
        if (session.getAttribute("authenticatedUser") == null) {
            return "User is not authenticated and cannot be logged out";
        }
        session.removeAttribute("authenticatedUser");
        return "User logged out";
    }
}