package com.example.plinko.controller;

import com.example.plinko.model.User;
import com.example.plinko.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Random;

@RestController
@RequestMapping("/game")
public class GameController {

    @Autowired
    private UserService userService;

    private static final int TOTAL_DROPS = 16;

    private static final Map<Integer, Double> MULTIPLIERS = new HashMap<>() {{
        put(0, 16.0);
        put(1, 9.0);
        put(2, 2.0);
        put(3, 1.4);
        put(4, 1.4);
        put(5, 1.2);
        put(6, 1.1);
        put(7, 1.0);
        put(8, 0.5);
        put(9, 1.0);
        put(10, 1.1);
        put(11, 1.2);
        put(12, 1.4);
        put(13, 1.4);
        put(14, 2.0);
        put(15, 9.0);
        put(16, 16.0);
    }};

    @PostMapping("/play/{userId}")
    public Map<String, Object> playGame(@PathVariable Long userId) {
        int outcome = 0;
        StringBuilder pattern = new StringBuilder();
        Random random = new Random();

        for (int i = 0; i < TOTAL_DROPS; i++) {
            if (random.nextBoolean()) {
                pattern.append("R");
                outcome++;
            } else {
                pattern.append("L");
            }
        }

        double multiplier = MULTIPLIERS.get(outcome);
        Optional<User> userOptional = userService.findById(userId);

        if (userOptional.isPresent()) {
            User user = userOptional.get();
            double newBalance = user.getBalance() * multiplier;
            user.setBalance(newBalance);
            userService.saveUser(user);

            Map<String, Object> response = new HashMap<>();
            response.put("pattern", pattern.toString());
            response.put("multiplier", multiplier);
            response.put("balance", newBalance);

            return response;
        } else {
            throw new RuntimeException("User not found");
        }
    }
}
