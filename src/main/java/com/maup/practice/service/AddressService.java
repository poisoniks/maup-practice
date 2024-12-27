package com.maup.practice.service;

import com.maup.practice.model.AddressModel;
import com.maup.practice.model.UserModel;

import java.util.List;

public interface AddressService {
    void saveAddress(AddressModel addressModel);
    List<AddressModel> findAddresses(UserModel userModel);
}
