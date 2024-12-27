package com.maup.practice.facade;

import com.maup.practice.dto.CheckoutRequestDTO;
import com.maup.practice.dto.OrderDetailsDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public interface CheckoutFacade {
    void checkout(CheckoutRequestDTO checkoutRequest);
    Page<OrderDetailsDTO> getOrders(Pageable pageable);
}
