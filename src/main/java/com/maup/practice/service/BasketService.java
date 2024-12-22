package com.maup.practice.service;

import com.maup.practice.model.BasketModel;
import com.maup.practice.model.UserModel;

public interface BasketService {
    BasketModel getOrCreateBasketForUser(UserModel user);

    void addToBasket(UserModel user, Long productId, int quantity);

    void removeFromBasket(UserModel user, Long productId);

    void clearBasket(UserModel user);
}
