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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

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
    @PostMapping("/login")
    public String login(@RequestBody  User loginRequest , HttpSession session) {
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
//    @GetMapping("/active-sessions")
//    public Object getActiveSessions(HttpSession session) {
//        Object userId = session.getAttribute("userId");
//        String sessionId = session.getId();
//
//        if (userId == null) {
//            return "Unauthorized: No user ID in session.";
//        }
//
//        UserSessionManager sessionManager = UserSessionManager.getInstance(redisTemplate);
//
//        // Validate: is the sessionId associated with that userId?
//        String storedUserId = sessionManager.getUserIdBySession(sessionId);
//        if (storedUserId == null || !storedUserId.equals(userId.toString())) {
//            return "Unauthorized: Invalid or expired session.";
//        }
//
//        // Authorized → return all active sessions
//        return sessionManager.getAllActiveUsers(); // This returns a Map<sessionId, userId>
//    }


}
