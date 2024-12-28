package com.maup.practice.util.converter;

import com.maup.practice.dto.OrderItemDTO;
import com.maup.practice.model.OrderItemModel;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class OrderItemModelToDTOConverter implements Converter<OrderItemModel, OrderItemDTO> {

    @Override
    public OrderItemDTO convert(OrderItemModel source) {
        if (source == null) {
            return null;
        }

        OrderItemDTO orderItemDTO = new OrderItemDTO();
        orderItemDTO.setProductName(source.getProduct().getName());
        orderItemDTO.setPrice(source.getProduct().getPrice().doubleValue());
        orderItemDTO.setQuantity(source.getQuantity());
        orderItemDTO.setTotalPrice(source.getPrice().doubleValue());
        return orderItemDTO;
    }
}
