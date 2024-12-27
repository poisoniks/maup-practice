package com.maup.practice.facade.impl;

import com.maup.practice.dto.CheckoutRequestDTO;
import com.maup.practice.dto.PaymentDTO;
import com.maup.practice.facade.CheckoutFacade;
import com.maup.practice.model.PaymentModel;
import com.maup.practice.service.CheckoutService;
import com.maup.practice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class CheckoutFacadeImpl implements CheckoutFacade {

    private final CheckoutService checkoutService;
    private final UserService userService;

    @Autowired
    public CheckoutFacadeImpl(CheckoutService checkoutService, UserService userService) {
        this.checkoutService = checkoutService;
        this.userService = userService;
    }

    @Override
    public void checkout(CheckoutRequestDTO checkoutRequest) {
        checkoutService.placeOrder(
                userService.getCurrentUser().getId(),
                checkoutRequest.getAddressId(),
                mapToPaymentModel(checkoutRequest.getPayment())
        );
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
