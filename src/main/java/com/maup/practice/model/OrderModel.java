package com.maup.practice.model;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
public class OrderModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserModel user;

    @Column(name = "order_date")
    private LocalDateTime orderDate;

    @Column(length = 50)
    private String status;

    @Column(precision = 10, scale = 2)
    private BigDecimal total;

    @ManyToOne
    @JoinColumn(name = "address_id")
    private AddressModel address;

    @OneToOne(mappedBy = "order", cascade = CascadeType.ALL)
    private PaymentModel payment;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItemModel> orderItems = new ArrayList<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UserModel getUser() {
        return user;
    }

    public void setUser(UserModel user) {
        this.user = user;
    }

    public LocalDateTime getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(LocalDateTime orderDate) {
        this.orderDate = orderDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public AddressModel getAddress() {
        return address;
    }

    public void setAddress(AddressModel address) {
        this.address = address;
    }

    public PaymentModel getPayment() {
        return payment;
    }

    public void setPayment(PaymentModel payment) {
        this.payment = payment;
    }

    public List<OrderItemModel> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(List<OrderItemModel> orderItems) {
        this.orderItems = orderItems;
    }
}
