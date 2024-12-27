package com.maup.practice.util.converter;

import com.maup.practice.dto.SupplierDTO;
import com.maup.practice.model.SupplierModel;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class SupplierModelToDTOConverter implements Converter<SupplierModel, SupplierDTO> {

    @Override
    public SupplierDTO convert(SupplierModel supplierModel) {
        if (supplierModel == null) {
            return null;
        }

        SupplierDTO supplierDTO = new SupplierDTO();
        supplierDTO.setId(supplierModel.getId());
        supplierDTO.setName(supplierModel.getName());
        supplierDTO.setAddress(supplierModel.getAddress());
        supplierDTO.setContactInfo(supplierModel.getContactInfo());
        return supplierDTO;
    }
}
