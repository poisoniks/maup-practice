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

    private static final String CATEGORIES_KEY = "categories";
    private static final String BRANDS_KEY = "brands";
    private static final String SUPPLIERS_KEY = "suppliers";
    private static final String PRODUCT_KEY = "product";
    private static final String PROFILE_KEY = "profile";
    private static final String REDIRECT_TO_HOMEPAGE = "redirect:/";
    private static final String PRODUCT_PAGE = "product";
    private static final String HOME_PAGE = "home";
    private static final String PROFILE_PAGE = "profile";
    private static final String SETTINGS_PAGE = "settings";
    private static final String CHECKOUT_PAGE = "checkout";
    private static final String ORDERS_PAGE = "orders";
    private static final String BACKOFFICE_PAGE = "backoffice";
    private static final String CONSOLE_PAGE = "console";

    private final ProductFacade productFacade;
    private final UserFacade userFacade;

    @Autowired
    public StaticPageController(ProductFacade productFacade, UserFacade userFacade) {
        this.productFacade = productFacade;
        this.userFacade = userFacade;
    }

    @GetMapping("/")
    public String root(Model model) {
        model.addAttribute(CATEGORIES_KEY, productFacade.findAllCategories());
        model.addAttribute(BRANDS_KEY, productFacade.findAllBrands());
        model.addAttribute(SUPPLIERS_KEY, productFacade.findAllSuppliers());
        return HOME_PAGE;
    }

    @GetMapping("/product/{id}")
    public String product(@PathVariable Long id, Model model) {
        ProductDTO product = productFacade.findProductById(id);
        if (product == null) {
            return REDIRECT_TO_HOMEPAGE;
        }
        model.addAttribute(PRODUCT_KEY, product);
        return PRODUCT_PAGE;
    }

    @GetMapping("/profile")
    public String profile(Model model) {
        UserProfileDTO userProfile = userFacade.getUserProfile();
        if (userProfile == null || userProfile.isAnonymous()) {
            return REDIRECT_TO_HOMEPAGE;
        }
        model.addAttribute(PROFILE_KEY, userProfile);
        return PROFILE_PAGE;
    }

    @GetMapping("/settings")
    public String settings() {
        UserProfileDTO userProfile = userFacade.getUserProfile();
        if (userProfile == null || userProfile.isAnonymous()) {
            return REDIRECT_TO_HOMEPAGE;
        }

        return SETTINGS_PAGE;
    }

    @GetMapping("/checkout")
    public String checkout() {
        return CHECKOUT_PAGE;
    }

    @GetMapping("/orders")
    public String orders() {
        return ORDERS_PAGE;
    }

    @GetMapping("/backoffice")
    public String backoffice() {
        return BACKOFFICE_PAGE;
    }

    @GetMapping("/console")
    public String console() {
        return CONSOLE_PAGE;
    }
}
