package com.maup.practice.service.impl;

import com.maup.practice.exception.ProductNotFoundException;
import com.maup.practice.model.BasketItemModel;
import com.maup.practice.model.BasketModel;
import com.maup.practice.model.ProductModel;
import com.maup.practice.model.UserModel;
import com.maup.practice.repository.BasketItemRepository;
import com.maup.practice.repository.BasketRepository;
import com.maup.practice.repository.ProductRepository;
import com.maup.practice.service.BasketService;

import com.maup.practice.service.UserService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BasketServiceImpl implements BasketService {

    @Autowired
    private BasketRepository basketRepository;

    @Autowired
    private BasketItemRepository basketItemRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserService userService;

    @Override
    public BasketModel getOrCreateBasketForUser(UserModel user) {
        BasketModel basket = basketRepository.findByUserId(user.getId());
        if (basket == null) {
            basket = new BasketModel();
            basket.setUser(user);
            basketRepository.save(basket);
        }
        return basket;
    }

    @Override
    public void addToBasket(UserModel user, Long productId, int quantity) {
        BasketModel basket = getOrCreateBasketForUser(user);
        ProductModel product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException("Product not found"));

        BasketItemModel item = basket.getItems().stream()
                .filter(basketItem -> basketItem.getProduct().getId().equals(productId))
                .findFirst()
                .orElse(new BasketItemModel());

        item.setBasket(basket);
        item.setProduct(product);
        item.setQuantity(item.getQuantity() + quantity);

        basket.getItems().add(item);
        basketRepository.save(basket);
    }

    @Override
    public void removeFromBasket(UserModel user, Long productId) {
        BasketModel basket = getOrCreateBasketForUser(user);

        basket.getItems().removeIf(item -> item.getProduct().getId().equals(productId));
        basketRepository.save(basket);
    }

    @Override
    public void clearBasket(UserModel user) {
        BasketModel basket = getOrCreateBasketForUser(user);
        basket.getItems().clear();
        basketRepository.save(basket);
    }

    @Transactional
    @Override
    public void mergeBaskets(UserModel user, BasketModel anonymousBasket) {
        BasketModel userBasket = getOrCreateBasketForUser(user);

        for (BasketItemModel item : anonymousBasket.getItems()) {
            item.setBasket(null);
            item.setBasket(userBasket);
            basketItemRepository.save(item);
            userBasket.getItems().add(item);
        }

        basketRepository.save(userBasket);

        user.setBasket(userBasket);
        userService.saveUser(user);
    }
}
