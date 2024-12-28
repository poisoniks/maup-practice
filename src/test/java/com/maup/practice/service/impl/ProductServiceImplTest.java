package com.maup.practice.service.impl;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.maup.practice.model.ProductModel;
import com.maup.practice.repository.BasketItemRepository;
import com.maup.practice.repository.OrderItemRepository;
import com.maup.practice.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class ProductServiceImplTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private BasketItemRepository basketItemRepository;

    @Mock
    private OrderItemRepository orderItemRepository;

    @InjectMocks
    private ProductServiceImpl productService;

    @Test
    void shouldFindProductsByFiltersWhenValidFiltersProvided() {
        BigDecimal minPrice = BigDecimal.valueOf(50);
        BigDecimal maxPrice = BigDecimal.valueOf(200);
        List<Long> brandIds = Arrays.asList(1L, 2L);
        List<Long> supplierIds = Arrays.asList(3L);
        List<Long> categoryIds = Arrays.asList(4L, 5L);
        String name = "Test Product";
        Pageable pageable = mock(Pageable.class);

        ProductModel product1 = new ProductModel();
        product1.setId(100L);
        ProductModel product2 = new ProductModel();
        product2.setId(101L);

        List<ProductModel> products = Arrays.asList(product1, product2);
        Page<ProductModel> productPage = new PageImpl<>(products);

        when(productRepository.findProductsByFilters(minPrice, maxPrice, brandIds, supplierIds, categoryIds, name, pageable))
                .thenReturn(productPage);

        Page<ProductModel> result = productService.findProductsByFilters(minPrice, maxPrice, brandIds, supplierIds, categoryIds, name, pageable);

        verify(productRepository, times(1)).findProductsByFilters(minPrice, maxPrice, brandIds, supplierIds, categoryIds, name, pageable);
        assertEquals(2, result.getContent().size());
        assertTrue(result.getContent().contains(product1));
        assertTrue(result.getContent().contains(product2));
    }

    @Test
    void shouldSaveProductWhenProductModelIsValid() {
        ProductModel product = new ProductModel();
        product.setId(200L);
        product.setName("New Product");

        when(productRepository.save(product)).thenReturn(product);

        ProductModel savedProduct = productService.saveProduct(product);

        verify(productRepository, times(1)).save(product);
        assertEquals(product, savedProduct);
    }

    @Test
    void shouldDeleteProductWhenProductIdExists() {
        Long productId = 300L;

        doNothing().when(productRepository).deleteById(productId);

        productService.deleteProduct(productId);

        verify(productRepository, times(1)).deleteById(productId);
    }

    @Test
    void shouldGetProductByIdWhenProductExists() {
        Long productId = 400L;
        ProductModel product = new ProductModel();
        product.setId(productId);
        product.setName("Existing Product");

        when(productRepository.findById(productId)).thenReturn(Optional.of(product));

        ProductModel foundProduct = productService.getProductById(productId);

        verify(productRepository, times(1)).findById(productId);
        assertEquals(product, foundProduct);
    }

    @Test
    void shouldGetProductByIdWhenProductDoesNotExist() {
        Long productId = 500L;

        when(productRepository.findById(productId)).thenReturn(Optional.empty());

        ProductModel foundProduct = productService.getProductById(productId);

        verify(productRepository, times(1)).findById(productId);
        assertNull(foundProduct);
    }

    @Test
    void shouldIsProductInUseWhenProductIsInBasket() {
        Long productId = 600L;
        ProductModel product = new ProductModel();
        product.setId(productId);

        when(productRepository.findById(productId)).thenReturn(Optional.of(product));
        when(basketItemRepository.existsByProduct(product)).thenReturn(true);

        boolean inUse = productService.isProductInUse(productId);

        verify(productRepository, times(1)).findById(productId);
        verify(basketItemRepository, times(1)).existsByProduct(product);
        assertTrue(inUse);
    }

    @Test
    void shouldIsProductInUseWhenProductIsInOrderItems() {
        Long productId = 700L;
        ProductModel product = new ProductModel();
        product.setId(productId);

        when(productRepository.findById(productId)).thenReturn(Optional.of(product));
        when(basketItemRepository.existsByProduct(product)).thenReturn(false);
        when(orderItemRepository.existsByProduct(product)).thenReturn(true);

        boolean inUse = productService.isProductInUse(productId);

        verify(productRepository, times(1)).findById(productId);
        verify(basketItemRepository, times(1)).existsByProduct(product);
        verify(orderItemRepository, times(1)).existsByProduct(product);
        assertTrue(inUse);
    }

    @Test
    void shouldIsProductInUseWhenProductIsNotInUse() {
        Long productId = 800L;
        ProductModel product = new ProductModel();
        product.setId(productId);

        when(productRepository.findById(productId)).thenReturn(Optional.of(product));
        when(basketItemRepository.existsByProduct(product)).thenReturn(false);
        when(orderItemRepository.existsByProduct(product)).thenReturn(false);

        boolean inUse = productService.isProductInUse(productId);

        verify(productRepository, times(1)).findById(productId);
        verify(basketItemRepository, times(1)).existsByProduct(product);
        verify(orderItemRepository, times(1)).existsByProduct(product);
        assertFalse(inUse);
    }
}
