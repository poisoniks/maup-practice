package com.maup.practice.service.impl;

import com.maup.practice.model.ProductModel;
import com.maup.practice.repository.BasketItemRepository;
import com.maup.practice.repository.OrderItemRepository;
import com.maup.practice.repository.ProductRepository;
import com.maup.practice.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private BasketItemRepository basketItemRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Override
    public Page<ProductModel> findProductsByFilters(BigDecimal minPrice, BigDecimal maxPrice, List<Long> brandIds, List<Long> supplierIds,
                                                    List<Long> categoryIds, String name, Pageable pageable) {
        return productRepository.findProductsByFilters(minPrice, maxPrice, brandIds, supplierIds, categoryIds, name, pageable);
    }

    @Override
    public ProductModel saveProduct(ProductModel productModel) {
        return productRepository.save(productModel);
    }

    @Override
    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }

    @Override
    public List<ProductModel> getAllProducts() {
        return productRepository.findAll();
    }

    @Override
    public ProductModel getProductById(Long id) {
        return productRepository.findById(id).orElse(null);
    }

    @Override
    public ProductModel getProductByName(String name) {
        return productRepository.findByName(name);
    }

    @Override
    public boolean isProductInUse(Long id) {
        return basketItemRepository.existsByProduct(productRepository.findById(id).orElse(null))
                || orderItemRepository.existsByProduct(productRepository.findById(id).orElse(null));
    }
}
