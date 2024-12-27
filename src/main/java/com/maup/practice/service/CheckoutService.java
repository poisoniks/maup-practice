package com.maup.practice.service;

import com.maup.practice.model.PaymentModel;

public interface CheckoutService {
    void placeOrder(Long userId, Long addressId, PaymentModel paymentInfo);
}
