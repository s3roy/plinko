package com.example.plinko.controller;

import com.example.plinko.model.User;
import com.example.plinko.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Base64;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public User registerUser(@RequestBody User user) {
        return userService.saveUser(user);
    }

    @GetMapping("/{id}")
    public Optional<User> getUser(@PathVariable Long id) {
        return userService.findById(id);
    }

    @PostMapping("/{id}/updateBalance")
    public User updateUserBalance(@PathVariable Long id, @RequestBody Double newBalance) {
        return userService.updateUserBalance(id, newBalance);
    }

    @PostMapping("/login")
    public Map<String, String> fastLogin(@RequestBody Map<String, String> loginRequest) {
        String username = loginRequest.get("username");
        User user = userService.findByUsername(username);
        if (user != null) {
            String token = generateToken(user);
            Map<String, String> response = new HashMap<>();
            response.put("token", token);
            return response;
        } else {
            throw new RuntimeException("User not found");
        }
    }

    private String generateToken(User user) {
        // Generate a simple token (In production, use a proper JWT token)
        return Base64.getEncoder().encodeToString((user.getUsername() + ":" + System.currentTimeMillis()).getBytes());
    }
}
