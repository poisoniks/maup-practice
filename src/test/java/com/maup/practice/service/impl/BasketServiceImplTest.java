package com.maup.practice.service.impl;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.maup.practice.exception.ProductNotFoundException;
import com.maup.practice.exception.StockExceededException;
import com.maup.practice.model.BasketItemModel;
import com.maup.practice.model.BasketModel;
import com.maup.practice.model.ProductModel;
import com.maup.practice.model.UserModel;
import com.maup.practice.repository.BasketItemRepository;
import com.maup.practice.repository.BasketRepository;
import com.maup.practice.repository.ProductRepository;
import com.maup.practice.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

@ExtendWith(MockitoExtension.class)
public class BasketServiceImplTest {

    @Mock
    private BasketRepository basketRepository;

    @Mock
    private BasketItemRepository basketItemRepository;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private UserService userService;

    @InjectMocks
    private BasketServiceImpl basketService;

    @Test
    void shouldGetOrCreateBasketForUserWhenBasketExists() {
        UserModel user = new UserModel();
        user.setId(1L);
        user.setEmail("user@example.com");

        BasketModel existingBasket = new BasketModel();
        existingBasket.setId(100L);
        existingBasket.setUser(user);
        existingBasket.setItems(Collections.emptySet());

        when(basketRepository.findByUserId(1L)).thenReturn(existingBasket);

        BasketModel basket = basketService.getOrCreateBasketForUser(user);

        verify(basketRepository, times(1)).findByUserId(1L);
        verify(basketRepository, never()).save(any());
        assertEquals(existingBasket, basket);
    }

    @Test
    void shouldGetOrCreateBasketForUserWhenBasketDoesNotExist() {
        UserModel user = new UserModel();
        user.setId(2L);
        user.setEmail("newuser@example.com");

        when(basketRepository.findByUserId(2L)).thenReturn(null);

        BasketModel newBasket = new BasketModel();
        newBasket.setId(200L);
        newBasket.setUser(user);
        newBasket.setItems(Collections.emptySet());

        when(basketRepository.save(any(BasketModel.class))).thenReturn(newBasket);

        BasketModel basket = basketService.getOrCreateBasketForUser(user);

        verify(basketRepository, times(1)).findByUserId(2L);
        verify(basketRepository, times(1)).save(any(BasketModel.class));
    }

    @Test
    void shouldAddToBasketWhenProductExistsAndStockAvailable() {
        UserModel user = new UserModel();
        user.setId(3L);
        user.setEmail("addtocart@example.com");

        Long productId = 10L;
        int quantity = 2;

        ProductModel product = new ProductModel();
        product.setId(productId);
        product.setStockQuantity(5);

        BasketModel basket = new BasketModel();
        basket.setId(300L);
        basket.setUser(user);
        basket.setItems(new HashSet<>());

        when(productRepository.findById(productId)).thenReturn(Optional.of(product));
        when(basketRepository.findByUserId(3L)).thenReturn(basket);
        when(basketRepository.save(basket)).thenReturn(basket);

        basketService.addToBasket(user, productId, quantity);

        ArgumentCaptor<BasketModel> basketCaptor = ArgumentCaptor.forClass(BasketModel.class);
        verify(basketRepository, times(1)).save(basketCaptor.capture());

        BasketModel savedBasket = basketCaptor.getValue();
        assertEquals(1, savedBasket.getItems().size());
        BasketItemModel item = savedBasket.getItems().iterator().next();
        assertEquals(product, item.getProduct());
        assertEquals(quantity, item.getQuantity());
    }

    @Test
    void shouldThrowProductNotFoundExceptionWhenProductDoesNotExist() {
        UserModel user = new UserModel();
        user.setId(4L);
        user.setEmail("nonexistent@example.com");

        Long productId = 20L;
        int quantity = 1;

        when(productRepository.findById(productId)).thenReturn(Optional.empty());

        assertThrows(ProductNotFoundException.class, () -> basketService.addToBasket(user, productId, quantity));
    }

    @Test
    void shouldThrowStockExceededExceptionWhenQuantityExceedsStock() {
        UserModel user = new UserModel();
        user.setId(5L);
        user.setEmail("stock@example.com");

        Long productId = 30L;
        int quantity = 10;

        ProductModel product = new ProductModel();
        product.setId(productId);
        product.setStockQuantity(5);

        BasketModel basket = new BasketModel();
        basket.setId(400L);
        basket.setUser(user);
        basket.setItems(Collections.emptySet());

        when(productRepository.findById(productId)).thenReturn(Optional.of(product));
        when(basketRepository.findByUserId(5L)).thenReturn(basket);

        assertThrows(StockExceededException.class, () -> basketService.addToBasket(user, productId, quantity));
    }

    @Test
    void shouldRemoveFromBasketWhenProductExistsInBasket() {
        UserModel user = new UserModel();
        user.setId(6L);
        user.setEmail("removefromcart@example.com");

        Long productId = 40L;

        ProductModel product = new ProductModel();
        product.setId(productId);

        BasketItemModel item = new BasketItemModel();
        item.setProduct(product);
        item.setQuantity(3);
        item.setBasket(new BasketModel());

        BasketModel basket = new BasketModel();
        basket.setId(500L);
        basket.setUser(user);
        Set<BasketItemModel> items = new HashSet<>();
        items.add(item);
        basket.setItems(items);

        when(basketRepository.findByUserId(6L)).thenReturn(basket);
        when(basketRepository.save(basket)).thenReturn(basket);

        basketService.removeFromBasket(user, productId);

        ArgumentCaptor<BasketModel> basketCaptor = ArgumentCaptor.forClass(BasketModel.class);
        verify(basketRepository, times(1)).save(basketCaptor.capture());

        BasketModel savedBasket = basketCaptor.getValue();
        assertTrue(savedBasket.getItems().isEmpty());
    }

    @Test
    void shouldClearBasketWhenCalled() {
        UserModel user = new UserModel();
        user.setId(7L);
        user.setEmail("clearcart@example.com");

        BasketItemModel item1 = new BasketItemModel();
        BasketItemModel item2 = new BasketItemModel();

        BasketModel basket = new BasketModel();
        basket.setId(600L);
        basket.setUser(user);
        Set<BasketItemModel> items = new HashSet<>();
        items.add(item1);
        items.add(item2);
        basket.setItems(items);

        when(basketRepository.findByUserId(7L)).thenReturn(basket);
        when(basketRepository.save(basket)).thenReturn(basket);

        basketService.clearBasket(user);

        ArgumentCaptor<BasketModel> basketCaptor = ArgumentCaptor.forClass(BasketModel.class);
        verify(basketRepository, times(1)).save(basketCaptor.capture());

        BasketModel savedBasket = basketCaptor.getValue();
        assertTrue(savedBasket.getItems().isEmpty());
    }

    @Test
    void shouldMergeBasketsWhenAnonymousBasketHasItems() {
        UserModel user = new UserModel();
        user.setId(8L);
        user.setEmail("merge@example.com");

        BasketModel userBasket = new BasketModel();
        userBasket.setId(700L);
        userBasket.setUser(user);
        userBasket.setItems(new HashSet<>());

        BasketModel anonymousBasket = new BasketModel();
        anonymousBasket.setId(800L);
        anonymousBasket.setUser(null);
        anonymousBasket.setItems(new HashSet<>());

        ProductModel product1 = new ProductModel();
        product1.setId(50L);
        product1.setStockQuantity(10);

        ProductModel product2 = new ProductModel();
        product2.setId(60L);
        product2.setStockQuantity(5);

        BasketItemModel item1 = new BasketItemModel();
        item1.setProduct(product1);
        item1.setQuantity(3);

        BasketItemModel item2 = new BasketItemModel();
        item2.setProduct(product2);
        item2.setQuantity(2);

        anonymousBasket.getItems().addAll(Arrays.asList(item1, item2));

        BasketItemModel existingItem = new BasketItemModel();
        existingItem.setProduct(product1);
        existingItem.setQuantity(2);
        userBasket.getItems().add(existingItem);

        when(basketRepository.findByUserId(8L)).thenReturn(userBasket);
        when(basketRepository.save(userBasket)).thenReturn(userBasket);
        when(basketItemRepository.save(any(BasketItemModel.class))).thenReturn(null);

        basketService.mergeBaskets(user, anonymousBasket);

        ArgumentCaptor<BasketModel> basketCaptor = ArgumentCaptor.forClass(BasketModel.class);
        verify(basketRepository, times(1)).save(basketCaptor.capture());
        verify(basketItemRepository, times(2)).save(any(BasketItemModel.class));
        verify(userService, times(1)).saveUser(user);

        BasketModel savedBasket = basketCaptor.getValue();
        assertEquals(2, savedBasket.getItems().size());

        BasketItemModel mergedItem1 = savedBasket.getItems().stream()
                .filter(item -> item.getProduct().getId().equals(50L))
                .findFirst()
                .orElse(null);
        assertNotNull(mergedItem1);
        assertEquals(5, mergedItem1.getQuantity());

        BasketItemModel mergedItem2 = savedBasket.getItems().stream()
                .filter(item -> item.getProduct().getId().equals(60L))
                .findFirst()
                .orElse(null);
        assertNotNull(mergedItem2);
        assertEquals(2, mergedItem2.getQuantity());
    }
}
