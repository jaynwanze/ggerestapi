package com.example.ggerestapi.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.ggerestapi.entity.User;
import com.example.ggerestapi.repository.UserRepository;
import com.example.ggerestapi.service.UserService;

import jakarta.servlet.http.HttpSession;

@RequestMapping("/api")
@Controller
public class UserController {
    @Autowired
    private UserService userService;

    @PutMapping(value = "/users/{id}")
    public String updateUser(@PathVariable long id, @RequestParam String name, @RequestParam String email,
            @RequestParam String password, RedirectAttributes redirectAttributes, HttpSession session) {
        return userService.updateUser(id, name, email, password, redirectAttributes, session);
    }

    @DeleteMapping(value = "/users/{id}")
    public String deleteUser(@PathVariable long id, HttpSession session, RedirectAttributes redirectAttributes) {
        return userService.deleteUser(id, session, redirectAttributes);
    }
}
