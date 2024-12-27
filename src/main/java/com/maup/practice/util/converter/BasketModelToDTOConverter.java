package com.maup.practice.util.converter;

import com.maup.practice.dto.BasketDTO;
import com.maup.practice.dto.BasketItemDTO;
import com.maup.practice.model.BasketItemModel;
import com.maup.practice.model.BasketModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

@Component
public class BasketModelToDTOConverter implements Converter<BasketModel, BasketDTO> {

    @Autowired
    private Converter<BasketItemModel, BasketItemDTO> basketItemConverter;

    @Override
    public BasketDTO convert(BasketModel source) {
        if (source == null) {
            return null;
        }

        BasketDTO basketDTO = new BasketDTO();
        basketDTO.setId(source.getId());
        basketDTO.setItems(convertItems(source.getItems()));
        return basketDTO;
    }

    private Set<BasketItemDTO> convertItems(Set<BasketItemModel> items) {
        return items.stream()
                .map(item -> basketItemConverter.convert(item))
                .collect(Collectors.toSet());
    }
}
