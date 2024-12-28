package com.maup.practice.service.impl;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.maup.practice.exception.CheckoutException;
import com.maup.practice.model.*;
import com.maup.practice.repository.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

@ExtendWith(MockitoExtension.class)
public class CheckoutServiceImplTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private AddressRepository addressRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private BasketRepository basketRepository;

    @Mock
    private PaymentRepository paymentRepository;

    @InjectMocks
    private CheckoutServiceImpl checkoutService;

    @Test
    void shouldPlaceOrderWhenValidUserAddressAndBasketWithItems() {
        Long userId = 1L;
        Long addressId = 10L;

        UserModel user = new UserModel();
        user.setId(userId);
        user.setEmail("user@example.com");

        AddressModel address = new AddressModel();
        address.setId(addressId);
        address.setCity("New York");
        address.setStreet("5th Avenue");
        address.setCountry("USA");
        address.setState("NY");
        address.setUser(user);

        PaymentModel paymentInfo = new PaymentModel();
        paymentInfo.setPaymentDate(LocalDateTime.now());
        paymentInfo.setAmount(new BigDecimal("150.00"));
        paymentInfo.setPaymentMethod("Credit Card");
        paymentInfo.setCardNumber("1234567890123456");
        paymentInfo.setCardHolder("John Doe");
        paymentInfo.setCardExpiry("12/25");

        BasketItemModel basketItem1 = new BasketItemModel();
        ProductModel product1 = new ProductModel();
        product1.setId(100L);
        product1.setPrice(new BigDecimal("50.00"));
        product1.setStockQuantity(10);
        basketItem1.setProduct(product1);
        basketItem1.setQuantity(2);

        BasketItemModel basketItem2 = new BasketItemModel();
        ProductModel product2 = new ProductModel();
        product2.setId(200L);
        product2.setPrice(new BigDecimal("25.00"));
        product2.setStockQuantity(5);
        basketItem2.setProduct(product2);
        basketItem2.setQuantity(2);

        BasketModel basket = new BasketModel();
        basket.setId(300L);
        basket.setUser(user);
        Set<BasketItemModel> items = new HashSet<>();
        items.add(basketItem1);
        items.add(basketItem2);
        basket.setItems(items);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(addressRepository.findById(addressId)).thenReturn(Optional.of(address));
        when(basketRepository.findByUserId(userId)).thenReturn(basket);
        when(productRepository.findById(100L)).thenReturn(Optional.of(product1));
        when(productRepository.findById(200L)).thenReturn(Optional.of(product2));

        checkoutService.placeOrder(userId, addressId, paymentInfo);

        ArgumentCaptor<OrderModel> orderCaptor = ArgumentCaptor.forClass(OrderModel.class);
        ArgumentCaptor<PaymentModel> paymentCaptor = ArgumentCaptor.forClass(PaymentModel.class);

        verify(orderRepository, times(1)).save(orderCaptor.capture());
        verify(paymentRepository, times(1)).save(paymentCaptor.capture());
        verify(basketRepository, times(1)).save(basket);

        OrderModel savedOrder = orderCaptor.getValue();
        assertEquals(user, savedOrder.getUser());
        assertEquals(address, savedOrder.getAddress());
        assertEquals(new BigDecimal("150.00"), savedOrder.getTotal());
        assertEquals("Placed", savedOrder.getStatus());
        assertNotNull(savedOrder.getOrderDate());

        List<OrderItemModel> orderItems = savedOrder.getOrderItems();
        assertEquals(2, orderItems.size());

        OrderItemModel orderItem1 = orderItems.get(0);
        assertEquals(2, orderItem1.getQuantity());

        OrderItemModel orderItem2 = orderItems.get(1);
        assertEquals(2, orderItem2.getQuantity());

        PaymentModel savedPayment = paymentCaptor.getValue();
        assertEquals(savedOrder, savedPayment.getOrder());
        assertEquals(paymentInfo.getAmount(), savedPayment.getAmount());
        assertEquals(paymentInfo.getPaymentMethod(), savedPayment.getPaymentMethod());
        assertEquals(paymentInfo.getCardNumber(), savedPayment.getCardNumber());
        assertEquals(paymentInfo.getCardHolder(), savedPayment.getCardHolder());
        assertEquals(paymentInfo.getCardExpiry(), savedPayment.getCardExpiry());

        assertTrue(basket.getItems().isEmpty());
    }

    @Test
    void shouldThrowCheckoutExceptionWhenUserDoesNotExist() {
        Long userId = 2L;
        Long addressId = 20L;

        PaymentModel paymentInfo = new PaymentModel();

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(CheckoutException.class, () -> checkoutService.placeOrder(userId, addressId, paymentInfo));
    }

    @Test
    void shouldThrowCheckoutExceptionWhenAddressDoesNotExist() {
        Long userId = 3L;
        Long addressId = 30L;

        UserModel user = new UserModel();
        user.setId(userId);
        user.setEmail("addressmissing@example.com");

        PaymentModel paymentInfo = new PaymentModel();

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(addressRepository.findById(addressId)).thenReturn(Optional.empty());

        assertThrows(CheckoutException.class, () -> checkoutService.placeOrder(userId, addressId, paymentInfo));
    }

    @Test
    void shouldThrowCheckoutExceptionWhenBasketIsEmpty() {
        Long userId = 4L;
        Long addressId = 40L;

        UserModel user = new UserModel();
        user.setId(userId);
        user.setEmail("emptybasket@example.com");

        AddressModel address = new AddressModel();
        address.setId(addressId);
        address.setUser(user);

        PaymentModel paymentInfo = new PaymentModel();

        BasketModel basket = new BasketModel();
        basket.setId(400L);
        basket.setUser(user);
        basket.setItems(new HashSet<>());

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(addressRepository.findById(addressId)).thenReturn(Optional.of(address));
        when(basketRepository.findByUserId(userId)).thenReturn(basket);

        assertThrows(CheckoutException.class, () -> checkoutService.placeOrder(userId, addressId, paymentInfo));
    }

    @Test
    void shouldThrowCheckoutExceptionWhenProductNotFound() {
        Long userId = 5L;
        Long addressId = 50L;

        UserModel user = new UserModel();
        user.setId(userId);
        user.setEmail("productmissing@example.com");

        AddressModel address = new AddressModel();
        address.setId(addressId);
        address.setUser(user);

        PaymentModel paymentInfo = new PaymentModel();

        BasketItemModel basketItem = new BasketItemModel();
        ProductModel product = new ProductModel();
        product.setId(500L);
        basketItem.setProduct(product);
        basketItem.setQuantity(1);

        BasketModel basket = new BasketModel();
        basket.setId(500L);
        basket.setUser(user);
        Set<BasketItemModel> items = new HashSet<>();
        items.add(basketItem);
        basket.setItems(items);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(addressRepository.findById(addressId)).thenReturn(Optional.of(address));
        when(basketRepository.findByUserId(userId)).thenReturn(basket);
        when(productRepository.findById(500L)).thenReturn(Optional.empty());

        assertThrows(CheckoutException.class, () -> checkoutService.placeOrder(userId, addressId, paymentInfo));
    }

    @Test
    void shouldReturnOrdersWhenUserHasOrders() {
        UserModel user = new UserModel();
        user.setId(6L);
        user.setEmail("ordersuser@example.com");

        Pageable pageable = mock(Pageable.class);

        OrderModel order1 = new OrderModel();
        order1.setId(600L);

        OrderModel order2 = new OrderModel();
        order2.setId(601L);

        List<OrderModel> orders = Arrays.asList(order1, order2);
        Page<OrderModel> orderPage = new PageImpl<>(orders);

        when(orderRepository.findAllByFilters(user.getId(), pageable)).thenReturn(orderPage);

        Page<OrderModel> result = checkoutService.getOrders(user, pageable);

        verify(orderRepository, times(1)).findAllByFilters(user.getId(), pageable);
        assertEquals(2, result.getContent().size());
        assertTrue(result.getContent().contains(order1));
        assertTrue(result.getContent().contains(order2));
    }

    @Test
    void shouldReturnEmptyPageWhenUserHasNoOrders() {
        UserModel user = new UserModel();
        user.setId(7L);
        user.setEmail("noorders@example.com");

        Pageable pageable = mock(Pageable.class);

        Page<OrderModel> orderPage = new PageImpl<>(Arrays.asList());

        when(orderRepository.findAllByFilters(user.getId(), pageable)).thenReturn(orderPage);

        Page<OrderModel> result = checkoutService.getOrders(user, pageable);

        verify(orderRepository, times(1)).findAllByFilters(user.getId(), pageable);
        assertNotNull(result);
        assertTrue(result.getContent().isEmpty());
    }
}
