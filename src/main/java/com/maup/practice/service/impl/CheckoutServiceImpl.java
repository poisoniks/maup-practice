package com.maup.practice.service.impl;

import com.maup.practice.exception.CheckoutException;
import com.maup.practice.model.*;
import com.maup.practice.repository.*;
import com.maup.practice.service.CheckoutService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class CheckoutServiceImpl implements CheckoutService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final AddressRepository addressRepository;
    private final UserRepository userRepository;
    private final BasketRepository basketRepository;
    private final PaymentRepository paymentRepository;

    @Autowired
    public CheckoutServiceImpl(OrderRepository orderRepository,
                               ProductRepository productRepository,
                               AddressRepository addressRepository,
                               UserRepository userRepository,
                               BasketRepository basketRepository,
                               PaymentRepository paymentRepository) {
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
        this.addressRepository = addressRepository;
        this.userRepository = userRepository;
        this.basketRepository = basketRepository;
        this.paymentRepository = paymentRepository;
    }

    public void placeOrder(Long userId, Long addressId, PaymentModel paymentInfo) {
        UserModel user = userRepository.findById(userId)
                .orElseThrow(() -> new CheckoutException("User not found"));

        AddressModel address = addressRepository.findById(addressId)
                .orElseThrow(() -> new CheckoutException("Address not found"));

        OrderModel order = new OrderModel();
        order.setUser(user);
        order.setOrderDate(LocalDateTime.now());
        order.setStatus("Placed");
        order.setAddress(address);
        order.setTotal(BigDecimal.ZERO);

        PaymentModel payment = new PaymentModel();
        payment.setPaymentDate(paymentInfo.getPaymentDate());
        payment.setAmount(paymentInfo.getAmount());
        payment.setPaymentMethod(paymentInfo.getPaymentMethod());
        payment.setCardNumber(paymentInfo.getCardNumber());
        payment.setCardHolder(paymentInfo.getCardHolder());
        payment.setCardExpiry(paymentInfo.getCardExpiry());
        payment.setOrder(order);

        BigDecimal total = BigDecimal.ZERO;
        List<OrderItemModel> orderItemModels = new ArrayList<>();
        BasketModel basket = basketRepository.findByUserId(user.getId());

        if (basket == null || basket.getItems().isEmpty()) {
            throw new CheckoutException("Basket is empty");
        }

        for (BasketItemModel item : basket.getItems()) {
            Long productId = item.getProduct().getId();
            Integer quantity = item.getQuantity();
            if (quantity == null || quantity <= 0) {
                continue;
            }

            ProductModel product = productRepository.findById(productId)
                    .orElseThrow(() -> new CheckoutException("Product not found"));

            BigDecimal itemPrice = product.getPrice().multiply(new BigDecimal(quantity));
            total = total.add(itemPrice);

            OrderItemModel oim = new OrderItemModel();
            oim.setOrder(order);
            oim.setProduct(product);
            oim.setQuantity(quantity);
            oim.setPrice(product.getPrice());

            orderItemModels.add(oim);
        }

        order.setTotal(total);

        order.getOrderItems().addAll(orderItemModels);

        orderRepository.save(order);
        paymentRepository.save(payment);

        basket.getItems().clear();
        basketRepository.save(basket);
    }

    @Override
    public Page<OrderModel> getOrders(UserModel user, Pageable pageable) {
        return orderRepository.findAllByFilters(user.getId(), pageable);
    }
}
