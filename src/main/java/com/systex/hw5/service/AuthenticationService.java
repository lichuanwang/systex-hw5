package com.systex.hw5.service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {

    private final UserService userService;

    @Autowired
    public AuthenticationService(UserService userService) {
        this.userService = userService;
    }

    public boolean authenticate(String username, String password) {
        return userService.authenticateUser(username, password);
    }

    public void createSession(HttpServletRequest request, String username) {
        HttpSession session = request.getSession(false); // Get existing session if it exists, do not create a new one yet
        if (session != null) {
            session.invalidate(); // Invalidate the old session if it exists
        }
        session = request.getSession(true); // Now create a new session
        session.setAttribute("user", username); // Set the username attribute in the new session
        session.setMaxInactiveInterval(30 * 60); // Set session timeout to 30 minutes
    }


    public void clearSession(HttpSession session) {
        if (session != null) {
            session.invalidate();
        }
    }
}
