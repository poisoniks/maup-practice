package com.maup.practice.dto;

import java.util.Objects;

public class CheckoutRequestDTO {
    private Long addressId;
    private PaymentDTO payment;

    public Long getAddressId() {
        return addressId;
    }

    public void setAddressId(Long addressId) {
        this.addressId = addressId;
    }

    public PaymentDTO getPayment() {
        return payment;
    }

    public void setPayment(PaymentDTO payment) {
        this.payment = payment;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        CheckoutRequestDTO that = (CheckoutRequestDTO) o;
        return Objects.equals(addressId, that.addressId) && Objects.equals(payment, that.payment);
    }

    @Override
    public int hashCode() {
        return Objects.hash(addressId, payment);
    }
}
