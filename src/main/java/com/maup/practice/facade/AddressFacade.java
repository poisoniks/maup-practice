package com.maup.practice.facade;

import com.maup.practice.dto.AddressDTO;

import java.util.List;

public interface AddressFacade {
    void addAddress(AddressDTO addressDTO);
    List<AddressDTO> getAddresses();
}
