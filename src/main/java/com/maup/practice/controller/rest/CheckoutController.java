package com.maup.practice.controller.rest;

import com.maup.practice.dto.CheckoutRequestDTO;
import com.maup.practice.exception.CheckoutException;
import com.maup.practice.facade.CheckoutFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/checkout")
public class CheckoutController {

    private final CheckoutFacade checkoutFacade;

    @Autowired
    public CheckoutController(CheckoutFacade checkoutFacade) {
        this.checkoutFacade = checkoutFacade;
    }

    @PostMapping
    public ResponseEntity<Void> checkout(@RequestBody CheckoutRequestDTO requestDTO) {
        try {
            checkoutFacade.checkout(requestDTO);
        } catch (CheckoutException e) {
            return ResponseEntity.internalServerError().build();
        }
        return ResponseEntity.ok().build();
    }
}
