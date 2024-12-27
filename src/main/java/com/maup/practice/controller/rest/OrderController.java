package com.maup.practice.controller.rest;

import com.maup.practice.dto.OrderDetailsDTO;
import com.maup.practice.facade.CheckoutFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    @Autowired
    private CheckoutFacade checkoutFacade;

    @GetMapping("/getOrders")
    public ResponseEntity<PagedModel<EntityModel<OrderDetailsDTO>>> getOrders(Pageable pageable, PagedResourcesAssembler<OrderDetailsDTO> assembler) {
        Page<OrderDetailsDTO> orders = checkoutFacade.getOrders(pageable);
        return ResponseEntity.ok(assembler.toModel(orders));
    }
}
