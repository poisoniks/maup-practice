package com.maup.practice.dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

public class OrderDetailsDTO {
    private Long id;
    private String status;
    private String paymentMethod;
    private String address;
    private LocalDateTime orderDate;
    private double total;
    private List<OrderItemDTO> orderItems;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public LocalDateTime getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(LocalDateTime orderDate) {
        this.orderDate = orderDate;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public List<OrderItemDTO> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(List<OrderItemDTO> orderItems) {
        this.orderItems = orderItems;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        OrderDetailsDTO that = (OrderDetailsDTO) o;
        return Double.compare(total, that.total) == 0 && Objects.equals(id, that.id) && Objects.equals(status, that.status) && Objects.equals(paymentMethod, that.paymentMethod) && Objects.equals(address, that.address) && Objects.equals(orderDate, that.orderDate) && Objects.equals(orderItems, that.orderItems);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, status, paymentMethod, address, orderDate, total, orderItems);
    }
}
