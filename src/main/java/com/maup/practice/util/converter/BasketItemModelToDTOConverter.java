package com.maup.practice.util.converter;

import com.maup.practice.dto.BasketItemDTO;
import com.maup.practice.dto.ProductDTO;
import com.maup.practice.model.BasketItemModel;
import com.maup.practice.model.ProductModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class BasketItemModelToDTOConverter implements Converter<BasketItemModel, BasketItemDTO> {

    @Autowired
    private Converter<ProductModel, ProductDTO> productConverter;

    @Override
    public BasketItemDTO convert(BasketItemModel source) {
        if (source == null) {
            return null;
        }

        BasketItemDTO basketItemDTO = new BasketItemDTO();
        basketItemDTO.setId(source.getId());
        basketItemDTO.setProduct(productConverter.convert(source.getProduct()));
        basketItemDTO.setQuantity(source.getQuantity());
        return basketItemDTO;
    }
}
