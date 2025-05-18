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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user/auth")
public class UserAuthController {

    @Autowired
    private  StringRedisTemplate redisTemplate;

    @Autowired
    private  UserService userService;

//    @Autowired
//    public UserAuthController(StringRedisTemplate redisTemplate, UserService userService) {
//        this.redisTemplate = redisTemplate;
//        this.userService = userService;
//    }
    @GetMapping("/login")
    public String login(@RequestParam String email, @RequestParam String password, HttpSession session) {
        User user = userService.getByEmail(email);

        if (user != null && user.getPassword().equals(password)) {
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
            return ResponseEntity.ok("Session is valid for userId: " + userId);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Session is invalid or expired");
        }
    }

}
