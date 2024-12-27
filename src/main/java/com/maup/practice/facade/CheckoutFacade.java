package com.maup.practice.facade;

import com.maup.practice.dto.CheckoutRequestDTO;
import org.springframework.stereotype.Service;

@Service
public interface CheckoutFacade {
    void checkout(CheckoutRequestDTO checkoutRequest);
}
