package com.maup.practice.util.converter;

import com.maup.practice.dto.BrandDTO;
import com.maup.practice.model.BrandModel;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class BrandModelToDTOConverter implements Converter<BrandModel, BrandDTO> {

    @Override
    public BrandDTO convert(BrandModel brandModel) {
        BrandDTO brandDTO = new BrandDTO();
        brandDTO.setId(brandModel.getId());
        brandDTO.setName(brandModel.getName());
        return brandDTO;
    }
}
