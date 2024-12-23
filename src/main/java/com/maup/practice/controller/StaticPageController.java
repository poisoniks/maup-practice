package com.maup.practice.controller;

import com.maup.practice.dto.RegistrationRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class StaticPageController {

    @GetMapping("/")
    public String root() {
        return "home";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/register")
    public String getRegisterPage(Model model) {
        RegistrationRequest userDto = new RegistrationRequest();
        model.addAttribute("user", userDto);
        return "register";
    }
}