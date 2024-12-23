package com.maup.practice.service.impl;

import com.maup.practice.model.ProductModel;
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

    @Override
    public Page<ProductModel> findProductsByFilters(BigDecimal minPrice, BigDecimal maxPrice, Long brandId, Long supplierId, Long categoryId, String name, Pageable pageable) {
        return productRepository.findProductsByFilters(minPrice, maxPrice, brandId, supplierId, categoryId, name, pageable);
    }

    @Override
    public ProductModel createProduct(ProductModel productModel) {
        return productRepository.save(productModel);
    }

    @Override
    public ProductModel updateProduct(ProductModel productModel) {
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
}
