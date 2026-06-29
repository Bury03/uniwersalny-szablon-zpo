package com.example.egzamin.controller;

import com.example.egzamin.entity.Role;
import com.example.egzamin.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/register")
    public String registerForm() {
        return "register";
    }

    @PostMapping("/register")
    public String register(@RequestParam String username,
                           @RequestParam String password,
                           Model model) {
        if (userService.existsByUsername(username)) {
            model.addAttribute("error", "Taki użytkownik już istnieje");
            return "register";
        }

        userService.createUser(username, password, Role.USER);
        return "redirect:/login?registered";
    }
}