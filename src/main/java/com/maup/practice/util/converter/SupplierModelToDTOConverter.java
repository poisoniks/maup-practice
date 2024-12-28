package com.maup.practice.util.converter;

import com.maup.practice.dto.SupplierDTO;
import com.maup.practice.model.SupplierModel;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class SupplierModelToDTOConverter implements Converter<SupplierModel, SupplierDTO> {

    @Override
    public SupplierDTO convert(SupplierModel source) {
        if (source == null) {
            return null;
        }

        SupplierDTO supplierDTO = new SupplierDTO();
        supplierDTO.setId(source.getId());
        supplierDTO.setName(source.getName());
        supplierDTO.setAddress(source.getAddress());
        supplierDTO.setContactInfo(source.getContactInfo());
        return supplierDTO;
    }
}
