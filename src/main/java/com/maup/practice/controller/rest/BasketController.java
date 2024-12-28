package com.maup.practice.controller.rest;

import com.maup.practice.dto.BasketDTO;
import com.maup.practice.exception.ProductNotFoundException;
import com.maup.practice.exception.StockExceededException;
import com.maup.practice.facade.BasketFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/basket")
public class BasketController {

    private final BasketFacade basketFacade;

    @Autowired
    public BasketController(BasketFacade basketFacade) {
        this.basketFacade = basketFacade;
    }

    @GetMapping("/get")
    public ResponseEntity<BasketDTO> getBasket() {
        return ResponseEntity.ok(basketFacade.getOrCreateBasket());
    }

    @PutMapping("/add")
    public ResponseEntity<String> addToBasket(
            @RequestParam Long productId,
            @RequestParam int quantity) {
        try {
            basketFacade.addToBasket(productId, quantity);
        } catch (ProductNotFoundException e) {
            return ResponseEntity.badRequest().body("Product not found.");
        } catch (StockExceededException e) {
            return ResponseEntity.badRequest().body("Requested quantity exceeds stock available.");
        }
        return ResponseEntity.ok("Product added to basket.");
    }

    @DeleteMapping("/remove")
    public ResponseEntity<String> removeFromBasket(
            @RequestParam Long productId) {
        basketFacade.removeFromBasket(productId);
        return ResponseEntity.ok("Product removed from basket.");
    }

    @DeleteMapping("/clear")
    public ResponseEntity<String> clearBasket() {
        basketFacade.clearBasket();
        return ResponseEntity.ok("Basket cleared.");
    }
}

