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

    @Autowired
    private Converter<OrderItemModel, OrderItemDTO> orderItemModelToDTOConverter;

    @Override
    public OrderDetailsDTO convert(OrderModel orderModel) {
        if (orderModel == null) {
            return null;
        }

        PaymentModel paymentModel = orderModel.getPayment();

        OrderDetailsDTO orderDetailsDTO = new OrderDetailsDTO();
        orderDetailsDTO.setId(orderModel.getId());
        orderDetailsDTO.setOrderDate(orderModel.getOrderDate());
        orderDetailsDTO.setTotal(orderModel.getTotal().doubleValue());
        if (paymentModel != null) {
            orderDetailsDTO.setPaymentMethod(paymentModel.getPaymentMethod());
        }
        orderDetailsDTO.setStatus(orderModel.getStatus());
        orderDetailsDTO.setAddress(mergeAddress(orderModel.getAddress()));
        orderDetailsDTO.setOrderItems(orderModel.getOrderItems().stream()
                .map(orderItemModelToDTOConverter::convert)
                .collect(Collectors.toList()));
        return orderDetailsDTO;
    }

    private String mergeAddress(AddressModel addressModel) {
        return addressModel.getStreet() + ", " + addressModel.getCity() + ", " + addressModel.getCountry();
    }
}
