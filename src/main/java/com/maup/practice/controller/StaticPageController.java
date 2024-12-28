package com.maup.practice.controller;

import com.maup.practice.dto.ProductDTO;
import com.maup.practice.dto.UserProfileDTO;
import com.maup.practice.facade.ProductFacade;
import com.maup.practice.facade.UserFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class StaticPageController {

    @Autowired
    private ProductFacade productFacade;

    @Autowired
    private UserFacade userFacade;

    @GetMapping("/")
    public String root(Model model) {
        model.addAttribute("categories", productFacade.findAllCategories());
        model.addAttribute("brands", productFacade.findAllBrands());
        model.addAttribute("suppliers", productFacade.findAllSuppliers());
        return "home";
    }

    @GetMapping("/product/{id}")
    public String product(@PathVariable Long id, Model model) {
        ProductDTO product = productFacade.findProductById(id);
        if (product == null) {
            return "redirect:/";
        }
        model.addAttribute("product", product);
        return "product";
    }

    @GetMapping("/profile")
    public String profile(Model model) {
        UserProfileDTO userProfile = userFacade.getUserProfile();
        if (userProfile == null || userProfile.isAnonymous()) {
            return "redirect:/";
        }
        model.addAttribute("profile", userProfile);
        return "profile";
    }

    @GetMapping("/settings")
    public String settings() {
        UserProfileDTO userProfile = userFacade.getUserProfile();
        if (userProfile == null || userProfile.isAnonymous()) {
            return "redirect:/";
        }

        return "settings";
    }

    @GetMapping("/checkout")
    public String checkout() {
        return "checkout";
    }

    @GetMapping("/orders")
    public String orders() {
        return "orders";
    }

    @GetMapping("/backoffice")
    public String backoffice() {
        return "backoffice";
    }

    @GetMapping("/console")
    public String console() {
        return "console";
    }
}
