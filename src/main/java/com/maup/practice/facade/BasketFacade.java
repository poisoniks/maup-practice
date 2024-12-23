package com.maup.practice.facade;

import com.maup.practice.dto.BasketDTO;

public interface BasketFacade {
    BasketDTO getOrCreateBasket();

    void addToBasket(Long productId, int quantity);

    void removeFromBasket(Long productId);

    void clearBasket();
}
