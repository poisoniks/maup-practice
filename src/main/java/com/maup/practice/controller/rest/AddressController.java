package com.maup.practice.controller.rest;

import com.maup.practice.dto.AddressDTO;
import com.maup.practice.facade.AddressFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/address")
public class AddressController {

    private final AddressFacade addressFacade;

    @Autowired
    public AddressController(AddressFacade addressFacade) {
        this.addressFacade = addressFacade;
    }

    @PostMapping("/add")
    public ResponseEntity<Void> add(@RequestBody AddressDTO addressDTO) {
        addressFacade.addAddress(addressDTO);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/get")
    public ResponseEntity<List<AddressDTO>> get() {
        return ResponseEntity.ok(addressFacade.getAddresses());
    }
}
