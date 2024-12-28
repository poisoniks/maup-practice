package com.maup.practice.util.converter;

import com.maup.practice.dto.OrderDetailsDTO;
import com.maup.practice.dto.OrderItemDTO;
import com.maup.practice.model.AddressModel;
import com.maup.practice.model.OrderItemModel;
import com.maup.practice.model.OrderModel;
import com.maup.practice.model.PaymentModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class OrderModelToDetailsDTOConverter implements Converter<OrderModel, OrderDetailsDTO> {

    private final Converter<OrderItemModel, OrderItemDTO> orderItemModelToDTOConverter;

    @Autowired
    public OrderModelToDetailsDTOConverter(Converter<OrderItemModel, OrderItemDTO> orderItemModelToDTOConverter) {
        this.orderItemModelToDTOConverter = orderItemModelToDTOConverter;
    }

    @Override
    public OrderDetailsDTO convert(OrderModel source) {
        if (source == null) {
            return null;
        }

        PaymentModel paymentModel = source.getPayment();

        OrderDetailsDTO orderDetailsDTO = new OrderDetailsDTO();
        orderDetailsDTO.setId(source.getId());
        orderDetailsDTO.setOrderDate(source.getOrderDate());
        orderDetailsDTO.setTotal(source.getTotal().doubleValue());
        if (paymentModel != null) {
            orderDetailsDTO.setPaymentMethod(paymentModel.getPaymentMethod());
        }
        orderDetailsDTO.setStatus(source.getStatus());
        orderDetailsDTO.setAddress(mergeAddress(source.getAddress()));
        orderDetailsDTO.setOrderItems(source.getOrderItems().stream()
                .map(orderItemModelToDTOConverter::convert)
                .collect(Collectors.toList()));
        return orderDetailsDTO;
    }

    private String mergeAddress(AddressModel addressModel) {
        return addressModel.getStreet() + ", " + addressModel.getCity() + ", " + addressModel.getCountry();
    }
}
