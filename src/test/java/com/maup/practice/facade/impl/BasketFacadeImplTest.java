package com.maup.practice.facade.impl;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.maup.practice.dto.BasketDTO;
import com.maup.practice.model.BasketModel;
import com.maup.practice.model.UserModel;
import com.maup.practice.service.BasketService;
import com.maup.practice.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.convert.converter.Converter;

@ExtendWith(MockitoExtension.class)
public class BasketFacadeImplTest {

    @Mock
    private UserService userService;

    @Mock
    private BasketService basketService;

    @Mock
    private Converter<BasketModel, BasketDTO> basketConverter;

    @InjectMocks
    private BasketFacadeImpl basketFacade;

    @Test
    void shouldGetOrCreateBasketWhenCalled() {
        UserModel currentUser = new UserModel();
        currentUser.setId(1L);
        currentUser.setEmail("user@example.com");

        BasketModel basketModel = new BasketModel();
        basketModel.setId(100L);

        BasketDTO basketDTO = new BasketDTO();
        basketDTO.setId(100L);

        when(userService.getCurrentUser()).thenReturn(currentUser);
        when(basketService.getOrCreateBasketForUser(currentUser)).thenReturn(basketModel);
        when(basketConverter.convert(basketModel)).thenReturn(basketDTO);

        BasketDTO result = basketFacade.getOrCreateBasket();

        verify(userService, times(1)).getCurrentUser();
        verify(basketService, times(1)).getOrCreateBasketForUser(currentUser);
        verify(basketConverter, times(1)).convert(basketModel);
        assertEquals(basketDTO, result);
    }

    @Test
    void shouldAddToBasketWhenProductIdAndQuantityAreValid() {
        Long productId = 50L;
        int quantity = 2;

        UserModel currentUser = new UserModel();
        currentUser.setId(2L);
        currentUser.setEmail("addtocart@example.com");

        when(userService.getCurrentUser()).thenReturn(currentUser);

        basketFacade.addToBasket(productId, quantity);

        verify(userService, times(1)).getCurrentUser();
        verify(basketService, times(1)).addToBasket(currentUser, productId, quantity);
    }

    @Test
    void shouldRemoveFromBasketWhenProductIdIsValid() {
        Long productId = 75L;

        UserModel currentUser = new UserModel();
        currentUser.setId(3L);
        currentUser.setEmail("removefromcart@example.com");

        when(userService.getCurrentUser()).thenReturn(currentUser);

        basketFacade.removeFromBasket(productId);

        verify(userService, times(1)).getCurrentUser();
        verify(basketService, times(1)).removeFromBasket(currentUser, productId);
    }

    @Test
    void shouldClearBasketWhenCalled() {
        UserModel currentUser = new UserModel();
        currentUser.setId(4L);
        currentUser.setEmail("clearcart@example.com");

        when(userService.getCurrentUser()).thenReturn(currentUser);

        basketFacade.clearBasket();

        verify(userService, times(1)).getCurrentUser();
        verify(basketService, times(1)).clearBasket(currentUser);
    }
}
