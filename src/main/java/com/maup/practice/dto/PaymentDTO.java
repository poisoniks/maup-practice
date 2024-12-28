package com.maup.practice.dto;

import java.math.BigDecimal;
import java.util.Objects;

public class PaymentDTO {
    private BigDecimal amount;
    private String paymentMethod;
    private String cardNumber;
    private String cardHolder;
    private String cardExpiry;

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getCardHolder() {
        return cardHolder;
    }

    public void setCardHolder(String cardHolder) {
        this.cardHolder = cardHolder;
    }

    public String getCardExpiry() {
        return cardExpiry;
    }

    public void setCardExpiry(String cardExpiry) {
        this.cardExpiry = cardExpiry;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        PaymentDTO that = (PaymentDTO) o;
        return Objects.equals(amount, that.amount) && Objects.equals(paymentMethod, that.paymentMethod) && Objects.equals(cardNumber, that.cardNumber) && Objects.equals(cardHolder, that.cardHolder) && Objects.equals(cardExpiry, that.cardExpiry);
    }

    @Override
    public int hashCode() {
        return Objects.hash(amount, paymentMethod, cardNumber, cardHolder, cardExpiry);
    }
}
