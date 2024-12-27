package com.maup.practice.dto;

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

}
