package com.maup.practice.facade.impl;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.maup.practice.dto.CheckoutRequestDTO;
import com.maup.practice.dto.OrderDetailsDTO;
import com.maup.practice.dto.PaymentDTO;
import com.maup.practice.model.OrderModel;
import com.maup.practice.model.PaymentModel;
import com.maup.practice.model.UserModel;
import com.maup.practice.service.CheckoutService;
import com.maup.practice.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

@ExtendWith(MockitoExtension.class)
public class CheckoutFacadeImplTest {

    @Mock
    private CheckoutService checkoutService;

    @Mock
    private UserService userService;

    @Mock
    private Converter<OrderModel, OrderDetailsDTO> orderConverter;

    @InjectMocks
    private CheckoutFacadeImpl checkoutFacade;

    @Test
    void shouldCheckoutWhenValidCheckoutRequest() {
        CheckoutRequestDTO checkoutRequest = new CheckoutRequestDTO();
        checkoutRequest.setAddressId(10L);
        PaymentDTO paymentDTO = new PaymentDTO();
        paymentDTO.setAmount(BigDecimal.valueOf(100.00));
        paymentDTO.setPaymentMethod("Credit Card");
        paymentDTO.setCardNumber("1234567890123456");
        paymentDTO.setCardHolder("John Doe");
        paymentDTO.setCardExpiry("12/25");
        checkoutRequest.setPayment(paymentDTO);

        UserModel currentUser = new UserModel();
        currentUser.setId(1L);
        currentUser.setEmail("user@example.com");

        when(userService.getCurrentUser()).thenReturn(currentUser);

        checkoutFacade.checkout(checkoutRequest);

        ArgumentCaptor<Long> userIdCaptor = ArgumentCaptor.forClass(Long.class);
        ArgumentCaptor<Long> addressIdCaptor = ArgumentCaptor.forClass(Long.class);
        ArgumentCaptor<PaymentModel> paymentModelCaptor = ArgumentCaptor.forClass(PaymentModel.class);

        verify(checkoutService, times(1)).placeOrder(userIdCaptor.capture(), addressIdCaptor.capture(), paymentModelCaptor.capture());

        assertEquals(1L, userIdCaptor.getValue());
        assertEquals(10L, addressIdCaptor.getValue());
        assertNotNull(paymentModelCaptor.getValue());
        assertEquals(BigDecimal.valueOf(100.00), paymentModelCaptor.getValue().getAmount());
        assertEquals("Credit Card", paymentModelCaptor.getValue().getPaymentMethod());
        assertEquals("1234567890123456", paymentModelCaptor.getValue().getCardNumber());
        assertEquals("John Doe", paymentModelCaptor.getValue().getCardHolder());
        assertEquals("12/25", paymentModelCaptor.getValue().getCardExpiry());
    }

    @Test
    void shouldGetOrdersWhenUserHasOrders() {
        Pageable pageable = mock(Pageable.class);
        UserModel currentUser = new UserModel();
        currentUser.setId(2L);
        currentUser.setEmail("orders@example.com");

        OrderModel order1 = new OrderModel();
        order1.setId(100L);
        OrderModel order2 = new OrderModel();
        order2.setId(101L);

        OrderDetailsDTO dto1 = new OrderDetailsDTO();
        dto1.setId(100L);
        OrderDetailsDTO dto2 = new OrderDetailsDTO();
        dto2.setId(101L);

        List<OrderModel> orders = Arrays.asList(order1, order2);
        Page<OrderModel> orderPage = new PageImpl<>(orders);

        when(userService.getCurrentUser()).thenReturn(currentUser);
        when(checkoutService.getOrders(currentUser, pageable)).thenReturn(orderPage);
        when(orderConverter.convert(order1)).thenReturn(dto1);
        when(orderConverter.convert(order2)).thenReturn(dto2);

        Page<OrderDetailsDTO> result = checkoutFacade.getOrders(pageable);

        verify(checkoutService, times(1)).getOrders(currentUser, pageable);
        verify(orderConverter, times(1)).convert(order1);
        verify(orderConverter, times(1)).convert(order2);
        assertEquals(2, result.getContent().size());
        assertTrue(result.getContent().contains(dto1));
        assertTrue(result.getContent().contains(dto2));
    }

    @Test
    void shouldGetOrdersWhenUserHasNoOrders() {
        Pageable pageable = mock(Pageable.class);
        UserModel currentUser = new UserModel();
        currentUser.setId(3L);
        currentUser.setEmail("noorders@example.com");

        Page<OrderModel> orderPage = new PageImpl<>(List.of());

        when(userService.getCurrentUser()).thenReturn(currentUser);
        when(checkoutService.getOrders(currentUser, pageable)).thenReturn(orderPage);

        Page<OrderDetailsDTO> result = checkoutFacade.getOrders(pageable);

        verify(checkoutService, times(1)).getOrders(currentUser, pageable);
        verifyNoInteractions(orderConverter);
        assertNotNull(result);
        assertTrue(result.getContent().isEmpty());
    }
}
