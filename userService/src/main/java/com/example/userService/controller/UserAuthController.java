package com.example.userService.controller;

import com.example.userService.command.Command;
import com.example.userService.command.LoginCommand;
import com.example.userService.command.LogoutCommand;
import com.example.userService.model.User;
import com.example.userService.service.UserService;
import com.example.userService.singleton.UserSessionManager;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping("/api/user/auth")
public class UserAuthController {

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public String login(@RequestBody User loginRequest, HttpSession session) {
        User user = userService.getByEmail(loginRequest.getEmail());

        if (user != null && user.getPassword().equals(loginRequest.getPassword())) {
            UserSessionManager sessionManager = UserSessionManager.getInstance(redisTemplate);
            Command loginCommand = new LoginCommand(user.getId(), session, sessionManager);
            return loginCommand.execute();
        } else {
            return "Invalid credentials!";
        }
    }

    @PostMapping("/logout")
    public String logout(HttpSession session) {
        UserSessionManager sessionManager = UserSessionManager.getInstance(redisTemplate);
        Command logoutCommand = new LogoutCommand(session, sessionManager);
        return logoutCommand.execute();
    }

    @GetMapping("/me")
    public ResponseEntity<String> validateSession(HttpSession session) {
        Object userId = session.getAttribute("userId");
        if (userId != null) {
            System.out.println("ME"+userId);
            return ResponseEntity.ok(userId.toString());

        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Session is invalid or expired");
        }
    }

    @GetMapping("/validate-session")
    public ResponseEntity<String> validateSession(@RequestParam String sessionId) {
        String userId = (String) redisTemplate.opsForValue().get(sessionId);

        if (userId != null) {
            return ResponseEntity.ok(userId);
        }

        return ResponseEntity.status(401).body("Invalid session");
    }

}
