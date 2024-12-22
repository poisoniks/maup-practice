package com.maup.practice.util;

import com.maup.practice.dto.SupplierDTO;
import com.maup.practice.model.SupplierModel;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class SupplierModelToDTOConverter implements Converter<SupplierModel, SupplierDTO> {

    @Override
    public SupplierDTO convert(SupplierModel supplierModel) {
        SupplierDTO supplierDTO = new SupplierDTO();
        supplierDTO.setId(supplierModel.getId());
        supplierDTO.setName(supplierModel.getName());
        return supplierDTO;
    }
}
