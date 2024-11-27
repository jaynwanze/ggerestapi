package com.example.ggerestapi.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.ggerestapi.entity.User;
import com.example.ggerestapi.repository.UserRepository;

import jakarta.servlet.http.HttpSession;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public String updateUser(@PathVariable long id, @RequestParam String name, @RequestParam String email,
            @RequestParam String password, RedirectAttributes redirectAttributes, HttpSession session) {
        User existingUser = userRepository.findById(id).orElse(null);
        if (existingUser == null) {
            redirectAttributes.addFlashAttribute("error", "User not found.");
            return "redirect:/profile";
        }
        existingUser.setName(name);
        existingUser.setEmail(email);
        existingUser.setPassword(password);
        userRepository.save(existingUser);
        User updatedUser = userRepository.findById(id).orElse(null);
        session.setAttribute("authenticatedUser", updatedUser);
        redirectAttributes.addFlashAttribute("success", "User updated.");
        return "redirect:/profile";
    }

    public String deleteUser(@PathVariable long id, HttpSession session, RedirectAttributes redirectAttributes) {
        User existingUser = userRepository.findById(id).orElse(null);
        if (existingUser == null) {
            redirectAttributes.addFlashAttribute("error", "User not found.");
            return "redirect:/profile";
        }
        userRepository.deleteById(id);
        User deletedUser = userRepository.findById(id).orElse(null);
        if (deletedUser == null) {
            session.removeAttribute("authenticatedUser");
            session.invalidate();
            redirectAttributes.addFlashAttribute("success", "User deleted successfully.");
            return "redirect:/login";
        }
        redirectAttributes.addFlashAttribute("error", "User not deleted.");
        return "redirect:/profile";
    }
    
    
}
