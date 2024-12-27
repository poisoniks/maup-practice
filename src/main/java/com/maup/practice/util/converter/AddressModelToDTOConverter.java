package com.maup.practice.util.converter;

import com.maup.practice.dto.AddressDTO;
import com.maup.practice.model.AddressModel;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class AddressModelToDTOConverter implements Converter<AddressModel, AddressDTO> {

    @Override
    public AddressDTO convert(AddressModel addressModel) {
        if (addressModel == null) {
            return null;
        }

        AddressDTO addressDTO = new AddressDTO();
        addressDTO.setId(addressModel.getId());
        addressDTO.setCity(addressModel.getCity());
        addressDTO.setStreet(addressModel.getStreet());
        addressDTO.setCountry(addressModel.getCountry());
        addressDTO.setState(addressModel.getState());

        return addressDTO;
    }
}
