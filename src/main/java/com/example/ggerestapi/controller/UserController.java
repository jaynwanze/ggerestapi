package com.example.ggerestapi.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.example.ggerestapi.entity.User;
import com.example.ggerestapi.repository.UserRepository;
import java.util.List;

@RequestMapping("/emissionsservice")
@RestController
public class UserController {
    @Autowired
    private UserRepository userRepository;

    @GetMapping(value = "/users", produces = "application/json")
    public List<User> getUsers() {
        return userRepository.findAll();
    }

    @GetMapping(value = "/users/{id}", produces = "application/json")
    public User getUser(@PathVariable long id) {
        return userRepository.findById(id).orElse(null);
    }

    @PostMapping(value = "/users", consumes = "application/json", produces = "application/json")
    public User createUser(@RequestBody User user) {
        return userRepository.save(user);
    }

    @PutMapping(value = "/users/{id}", consumes = "application/json", produces = "application/json")
    public User updateUser(@PathVariable long id, @RequestBody User user) {
        User existingUser = userRepository.findById(id).orElse(null);
        if (existingUser != null) {
            existingUser.setName(user.getName());
            existingUser.setEmail(user.getEmail());
            existingUser.setPassword(user.getPassword());
            return userRepository.save(existingUser);
        }
        return null;
    }

    @DeleteMapping(value = "/users/{id}", produces = "text/plain")
    public String deleteUser(@PathVariable long id) {
        User existingUser = userRepository.findById(id).orElse(null);
        if (existingUser == null) {
            return "User not found";
        }
        userRepository.deleteById(id);
        return "User deleted successfully";
    }
}
