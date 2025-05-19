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
    public ResponseEntity<Map<String, String>> login(@RequestBody User loginRequest, HttpSession session) {
        User user = userService.getByEmail(loginRequest.getEmail());

        if (user != null && user.getPassword().equals(loginRequest.getPassword())) {
            UserSessionManager sessionManager = UserSessionManager.getInstance(redisTemplate);
            Command loginCommand = new LoginCommand(user.getId(), session, sessionManager);
            String response = loginCommand.execute();

            // Add the session ID and user ID to activeUsers
            String sessionId = session.getId();
            sessionManager.addActiveUser(user.getId(), sessionId);
            System.out.println("User " + user.getId() + " logged in with session " + sessionId);

            // Prepare the response
            Map<String, String> responseBody = new HashMap<>();
            responseBody.put("sessionId", sessionId);
            responseBody.put("userId", user.getId());
            responseBody.put("message", response);

            return ResponseEntity.ok(responseBody);
        } else {
            // Prepare the error response
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("message", "Invalid credentials!");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
        }
    }


    @PostMapping("/logout")
    public String logout(HttpSession session) {
        UserSessionManager sessionManager = UserSessionManager.getInstance(redisTemplate);
        Command logoutCommand = new LogoutCommand(session, sessionManager);
        return logoutCommand.execute();
    }

    @GetMapping("/me")
    public ResponseEntity<String> validateSession(@RequestParam String sessionId) {
        // Check if the session ID exists in the activeUsers hash
        String userId = (String) redisTemplate.opsForHash().entries("activeUsers").entrySet().stream()
                .filter(entry -> entry.getKey().equals(sessionId))
                .map(entry -> (String) entry.getValue())
                .findFirst()
                .orElse(null);

        if (userId != null) {
            return ResponseEntity.ok(userId);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Session is invalid or expired");
        }
    }




}
