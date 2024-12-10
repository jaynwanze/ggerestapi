package com.example.ggerestapi.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.ggerestapi.entity.User;
import com.example.ggerestapi.repository.UserRepository;
import com.example.ggerestapi.service.AuthService;
import com.example.ggerestapi.service.UserService;

import jakarta.servlet.http.HttpSession;

@RequestMapping("/api")
@Controller
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @PutMapping(value = "/users/{id}")
    public String updateUser(@PathVariable long id, @RequestParam String name, @RequestParam String email,
            @RequestParam String password, RedirectAttributes redirectAttributes, HttpSession session) {
        return userService.updateUser(id, name, email, password, redirectAttributes, session);
    }

    @DeleteMapping(value = "/users/{id}")
    public String deleteUser(@PathVariable long id, HttpSession session, RedirectAttributes redirectAttributes) {
        return userService.deleteUser(id, session, redirectAttributes);
    }

    @GetMapping(value = "/json/users", produces = "application/json")
    @ResponseBody
    public ResponseEntity<?> jsonGetUsers(HttpSession session) {
        if (session.getAttribute("authenticatedUser") == null) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "User is not authenticated. Please log in.");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
        }
        return ResponseEntity.ok(userRepository.findAll());
    }

    @GetMapping(value = "/json/user", produces = "application/json")
    @ResponseBody
    public ResponseEntity<?> jsonGetUser(HttpSession session) {
        User user = (User) session.getAttribute("authenticatedUser");
        if (user == null) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "User is not authenticated. Please log in.");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
        }

        return ResponseEntity.ok(userRepository.findById(user.getId()).orElse(null));
    }

    @PutMapping(value = "/json/users", consumes = "application/json", produces = "application/json")
    @ResponseBody
    public String jsonUpdateUser(@RequestBody User user, HttpSession session) {
        User currentUser = (User) session.getAttribute("authenticatedUser");
        if (currentUser == null) {
            return "User is not authenticated. Please log in.";
        }
        currentUser.setName(user.getName());
        currentUser.setEmail(user.getEmail());
        currentUser.setPassword(user.getPassword());
        userRepository.save(currentUser);
        return "User updated";
    }

    @DeleteMapping(value = "/json/users", produces = "text/plain")
    @ResponseBody
    public String jsonDeleteUser(HttpSession session) {
        User user = (User) session.getAttribute("authenticatedUser");
        if (user == null) {
            return "User is not authenticated. Please log in.";
        }
        userRepository.delete(user);
        return "User deleted";
    }
}
