package com.systex.hw4.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
public class HomeController {

    @GetMapping({"/", "/home"})
    public String home() {
        return "index"; // This will forward to WEB-INF/views/index.jsp
    }
}