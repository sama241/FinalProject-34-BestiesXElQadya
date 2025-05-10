package com.example.userService.command;

import com.example.userService.singleton.UserSessionManager;
import jakarta.servlet.http.HttpSession;

import java.util.UUID;

public class LogoutCommand implements Command {

    private final HttpSession session;
    private final UserSessionManager sessionManager;

    public LogoutCommand(HttpSession session, UserSessionManager sessionManager) {
        this.session = session;
        this.sessionManager = sessionManager;
    }

    @Override
    public String execute() {
        Object userId = session.getAttribute("userId");

        if (userId != null) {
            sessionManager.removeActiveUser(UUID.fromString(userId.toString()));
            session.invalidate();
            return "User logged out successfully!";
        } else {
            return "No user is logged in.";
        }
    }
}
