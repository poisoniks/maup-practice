package com.maup.practice.facade.impl;

import com.maup.practice.dto.BasketDTO;
import com.maup.practice.facade.BasketFacade;
import com.maup.practice.model.BasketModel;
import com.maup.practice.model.UserModel;
import com.maup.practice.service.BasketService;
import com.maup.practice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Service;

@Service
public class BasketFacadeImpl implements BasketFacade {

    private final UserService userService;
    private final BasketService basketService;
    private final Converter<BasketModel, BasketDTO> basketConverter;

    @Autowired
    public BasketFacadeImpl(UserService userService, BasketService basketService, Converter<BasketModel, BasketDTO> basketConverter) {
        this.userService = userService;
        this.basketService = basketService;
        this.basketConverter = basketConverter;
    }

    @Override
    public BasketDTO getOrCreateBasket() {
        return basketConverter.convert(basketService.getOrCreateBasketForUser(userService.getCurrentUser()));
    }

    @Override
    public void addToBasket(Long productId, int quantity) {
        UserModel user = userService.getCurrentUser();
        basketService.addToBasket(user, productId, quantity);
    }

    @Override
    public void removeFromBasket(Long productId) {
        UserModel user = userService.getCurrentUser();
        basketService.removeFromBasket(user, productId);
    }

    @Override
    public void clearBasket() {
        UserModel user = userService.getCurrentUser();
        basketService.clearBasket(user);
    }
}
