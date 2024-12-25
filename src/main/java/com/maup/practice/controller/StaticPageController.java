package com.maup.practice.controller;

import com.maup.practice.facade.ProductFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class StaticPageController {

    @Autowired
    private ProductFacade productFacade;

    @GetMapping("/")
    public String root(Model model) {
        model.addAttribute("categories", productFacade.findAllCategories());
        model.addAttribute("brands", productFacade.findAllBrands());
        model.addAttribute("suppliers", productFacade.findAllSuppliers());
        return "home";
    }
}