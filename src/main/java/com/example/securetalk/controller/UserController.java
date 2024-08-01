package com.example.securetalk.controller;

import com.example.securetalk.model.User;
import com.example.securetalk.service.SessionService;
import com.example.securetalk.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private SessionService sessionService;

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody User user, HttpSession session) {
        if (userService.validateUser(user.getUsername(), user.getPassword())) {
            Optional<User> validUserOpt = userService.findByUsername(user.getUsername());
            if (validUserOpt.isPresent()) {
                User validUser = validUserOpt.get();
                session.setAttribute("username", validUser.getUsername());
                session.setAttribute("userId", validUser.getId());

                LocalDateTime expiresAt = LocalDateTime.now().plus(1, ChronoUnit.DAYS); // 세션 만료 시간을 1일로 설정
                sessionService.saveSession(validUser.getId(), session.getId(), expiresAt);

                return ResponseEntity.ok("Login successful");
            } else {
                return ResponseEntity.status(401).body("User not found");
            }
        } else {
            return ResponseEntity.status(401).body("Invalid credentials");
        }
    }

    @GetMapping("/session")
    public ResponseEntity<?> getSession(HttpSession session) {
        String username = (String) session.getAttribute("username");
        if (username != null) {
            return ResponseEntity.ok().body(Map.of("username", username));
        } else {
            return ResponseEntity.status(401).body("No user logged in");
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logoutUser(HttpSession session) {
        session.invalidate();
        return ResponseEntity.ok("Logout successful");
    }
}
