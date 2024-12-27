package com.maup.practice.facade.impl;

import com.maup.practice.dto.CheckoutRequestDTO;
import com.maup.practice.dto.OrderDetailsDTO;
import com.maup.practice.dto.PaymentDTO;
import com.maup.practice.facade.CheckoutFacade;
import com.maup.practice.model.OrderModel;
import com.maup.practice.model.PaymentModel;
import com.maup.practice.service.CheckoutService;
import com.maup.practice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class CheckoutFacadeImpl implements CheckoutFacade {

    private final CheckoutService checkoutService;
    private final UserService userService;
    private final Converter<OrderModel, OrderDetailsDTO> orderConverter;

    @Autowired
    public CheckoutFacadeImpl(CheckoutService checkoutService, UserService userService, Converter<OrderModel, OrderDetailsDTO> orderConverter) {
        this.checkoutService = checkoutService;
        this.userService = userService;
        this.orderConverter = orderConverter;
    }

    @Override
    public void checkout(CheckoutRequestDTO checkoutRequest) {
        checkoutService.placeOrder(
                userService.getCurrentUser().getId(),
                checkoutRequest.getAddressId(),
                mapToPaymentModel(checkoutRequest.getPayment())
        );
    }

    @Override
    public Page<OrderDetailsDTO> getOrders(Pageable pageable) {
        return checkoutService.getOrders(userService.getCurrentUser(), pageable)
                .map(orderConverter::convert);
    }

    private PaymentModel mapToPaymentModel(PaymentDTO paymentDTO) {
        PaymentModel pm = new PaymentModel();
        pm.setPaymentDate(LocalDateTime.now());
        pm.setAmount(paymentDTO.getAmount());
        pm.setPaymentMethod(paymentDTO.getPaymentMethod());
        pm.setCardNumber(paymentDTO.getCardNumber());
        pm.setCardHolder(paymentDTO.getCardHolder());
        pm.setCardExpiry(paymentDTO.getCardExpiry());
        return pm;
    }
}
