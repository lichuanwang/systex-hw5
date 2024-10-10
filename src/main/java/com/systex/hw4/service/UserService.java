package com.systex.hw4.service;

import com.systex.hw4.model.User;
import com.systex.hw4.model.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;  // Add Password Encoder

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;  // Inject Password Encoder
    }

    public boolean registerUser(User user) {
        // Encode the password before saving
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);  // Set the encoded password

        // Save the user with the encoded password
        userRepository.save(user);
        return true;
    }

    public boolean authenticateUser(String username, String rawPassword) {
        // Find the user by username
        return userRepository.findByUsername(username)
                .map(user -> passwordEncoder.matches(rawPassword, user.getPassword())) // Match raw password with encoded password
                .orElse(false);  // Return false if the user is not found
    }

    public boolean authenticateUser(String username) {
        return userRepository.findByUsername(username).isPresent();
    }
}