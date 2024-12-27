package com.maup.practice.util.converter;

import com.maup.practice.dto.OrderItemDTO;
import com.maup.practice.model.OrderItemModel;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class OrderItemModelToDTOConverter implements Converter<OrderItemModel, OrderItemDTO> {

    @Override
    public OrderItemDTO convert(OrderItemModel orderItemModel) {
        if (orderItemModel == null) {
            return null;
        }

        OrderItemDTO orderItemDTO = new OrderItemDTO();
        orderItemDTO.setProductName(orderItemModel.getProduct().getName());
        orderItemDTO.setPrice(orderItemModel.getProduct().getPrice().doubleValue());
        orderItemDTO.setQuantity(orderItemModel.getQuantity());
        orderItemDTO.setTotalPrice(orderItemModel.getPrice().doubleValue());
        return orderItemDTO;
    }
}
