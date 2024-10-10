//package com.systex.hw4.utils;
//
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//public class PasswordUtils {
//
//    // BCrypt hashing function
//    public static String hashPassword(String password) {
//        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
//        return passwordEncoder.encode(password);
//    }
//
//    // Verifying the password
//    public static boolean verifyPassword(String rawPassword, String hashedPassword) {
//        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
//        return passwordEncoder.matches(rawPassword, hashedPassword);
//    }
//}
