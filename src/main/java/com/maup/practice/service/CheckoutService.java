package com.maup.practice.service;

import com.maup.practice.model.OrderModel;
import com.maup.practice.model.PaymentModel;
import com.maup.practice.model.UserModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CheckoutService {
    void placeOrder(Long userId, Long addressId, PaymentModel paymentInfo);
    Page<OrderModel> getOrders(UserModel user, Pageable pageable);
}
