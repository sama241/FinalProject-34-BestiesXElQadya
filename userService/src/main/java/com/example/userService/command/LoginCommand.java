package com.example.userService.command;

import com.example.userService.singleton.UserSessionManager;
import jakarta.servlet.http.HttpSession;

import java.util.UUID;

public class LoginCommand implements Command {

    private final UUID userId;
    private final HttpSession session;
    private final UserSessionManager sessionManager;

    public LoginCommand(UUID userId, HttpSession session, UserSessionManager sessionManager) {
        this.userId = userId;
        this.session = session;
        this.sessionManager = sessionManager;

    }

    @Override
    public String execute() {
        if (sessionManager.isUserActive(userId)) {
            return "User is already logged in!";
        }
        session.setAttribute("userId", userId);
        session.setAttribute("loginTime", System.currentTimeMillis());

        System.out.println("Session Attributes:");
        session.getAttributeNames().asIterator().forEachRemaining(name -> {
            System.out.println(name + " = " + session.getAttribute(name));
        });

        session.setAttribute("userId", userId);
        sessionManager.addActiveUser(session.getId());
        return "User logged in successfully!";
    }
}
