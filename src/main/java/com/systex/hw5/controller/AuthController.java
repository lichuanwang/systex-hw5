package com.systex.hw5.controller;

import com.systex.hw5.model.User;
import com.systex.hw5.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/auth")
public class AuthController {

    private final UserService userService;

    @Autowired
    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/login")
    public String showLoginPage(Model model) {
        return "auth/newlogin";  // This renders the login page
    }

    @PostMapping("/login")
    public String loginUser() {
        // Since login is handled in the filter, this POST handler is not required.
        // However, if the filter forwards requests back here, it will just re-display the login page
        return "auth/newlogin";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();  // Invalidate the session to log out the user
        return "redirect:/auth/login";  // Redirect to the login page after logout
    }

    // Show the registration page
    @GetMapping("/register")
    public String showRegisterPage(Model model) {
        return "auth/register";  // Render the registration page
    }

    // Handle the registration form submission
    @PostMapping("/register")
    public String registerUser(@RequestParam String username,
                               @RequestParam String email,
                               @RequestParam String password,
                               @RequestParam String confirmPassword,
                               Model model) {
        // Validate passwords
        if (!password.equals(confirmPassword)) {
            model.addAttribute("error", "Passwords do not match.");
            return "auth/register";  // Reload the registration page with error
        }

        // Check if the username is already taken
        if (userService.authenticateUser(username)) {
            model.addAttribute("error", "Username already exists.");
            return "auth/register";  // Reload the registration page with error
        }

        // Register the new user
        User newUser = new User();
        newUser.setUsername(username);
        newUser.setEmail(email);
        newUser.setPassword(password);  // Password encoding is handled in the service

        userService.registerUser(newUser);

        // After successful registration, redirect to the login page
        return "redirect:/auth/login";
    }

}
