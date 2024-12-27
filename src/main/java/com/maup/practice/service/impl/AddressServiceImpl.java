package com.maup.practice.service.impl;

import com.maup.practice.model.AddressModel;
import com.maup.practice.model.UserModel;
import com.maup.practice.repository.AddressRepository;
import com.maup.practice.service.AddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AddressServiceImpl implements AddressService {

    @Autowired
    private AddressRepository addressRepository;

    @Override
    public void saveAddress(AddressModel addressModel) {
        addressRepository.save(addressModel);
    }

    @Override
    public List<AddressModel> findAddresses(UserModel userModel) {
        return addressRepository.findByUser(userModel);
    }
}
