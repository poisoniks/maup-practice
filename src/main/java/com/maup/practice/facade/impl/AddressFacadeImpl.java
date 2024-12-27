package com.maup.practice.facade.impl;

import com.maup.practice.dto.AddressDTO;
import com.maup.practice.facade.AddressFacade;
import com.maup.practice.model.AddressModel;
import com.maup.practice.service.AddressService;
import com.maup.practice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AddressFacadeImpl implements AddressFacade {

    @Autowired
    private AddressService addressService;

    @Autowired
    private UserService userService;

    @Autowired
    private Converter<AddressModel, AddressDTO> addressConverter;

    @Override
    public void addAddress(AddressDTO addressDTO) {
        AddressModel addressModel = new AddressModel();
        addressModel.setCity(addressDTO.getCity());
        addressModel.setStreet(addressDTO.getStreet());
        addressModel.setCountry(addressDTO.getCountry());
        addressModel.setState(addressDTO.getState());
        addressModel.setUser(userService.getCurrentUser());
        addressService.saveAddress(addressModel);
    }

    @Override
    public List<AddressDTO> getAddresses() {
        return addressService.findAddresses(userService.getCurrentUser())
                .stream()
                .map(addressConverter::convert)
                .collect(Collectors.toList());
    }
}
