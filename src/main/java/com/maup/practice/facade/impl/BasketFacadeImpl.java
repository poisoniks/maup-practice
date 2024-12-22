package com.maup.practice.facade.impl;

import com.maup.practice.dto.BasketDTO;
import com.maup.practice.facade.BasketFacade;
import com.maup.practice.model.BasketModel;
import com.maup.practice.model.UserModel;
import com.maup.practice.service.BasketService;
import com.maup.practice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class BasketFacadeImpl implements BasketFacade {

    @Autowired
    private UserService userService;

    @Autowired
    private BasketService basketService;

    @Autowired
    private Converter<BasketModel, BasketDTO> basketConverter;

    @Override
    public BasketDTO getOrCreateBasket() {
        return basketConverter.convert(basketService.getOrCreateBasketForUser(getCurrentUser()));
    }

    @Override
    public void addToBasket(Long productId, int quantity) {
        UserModel user = getCurrentUser();
        basketService.addToBasket(user, productId, quantity);
    }

    @Override
    public void removeFromBasket(Long productId) {
        UserModel user = getCurrentUser();
        basketService.removeFromBasket(user, productId);
    }

    @Override
    public void clearBasket() {
        UserModel user = getCurrentUser();
        basketService.clearBasket(user);
    }

    private UserModel getCurrentUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return userService.findByUsername(username);
    }
}
