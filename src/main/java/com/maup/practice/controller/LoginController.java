package com.maup.practice.controller;

import com.maup.practice.dto.UserRegistrationForm;
import com.maup.practice.exception.EmailExistsException;
import com.maup.practice.facade.LoginFacade;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class LoginController {

    @Autowired
    private LoginFacade loginFacade;

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/register")
    public String getRegisterPage(Model model) {
        UserRegistrationForm userDto = new UserRegistrationForm();
        model.addAttribute("user", userDto);
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(@ModelAttribute("user") @Valid UserRegistrationForm user, BindingResult result) {
        if (result.hasErrors()) {
            return "register";
        }

        try {
            loginFacade.registerUser(user);
        } catch (EmailExistsException e) {
            result.rejectValue("email", "email", e.getMessage());
            return "register";
        }

        return "login";
    }
}
