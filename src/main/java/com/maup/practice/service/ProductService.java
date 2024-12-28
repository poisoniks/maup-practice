package com.maup.practice.service;

import com.maup.practice.model.ProductModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.List;

public interface ProductService {
    Page<ProductModel> findProductsByFilters(BigDecimal minPrice, BigDecimal maxPrice, List<Long> brandIds, List<Long> supplierIds,
                                             List<Long> categoryIds, String name, Pageable pageable);

    ProductModel saveProduct(ProductModel productModel);

    void deleteProduct(Long id);

    ProductModel getProductById(Long id);

    boolean isProductInUse(Long id);
}
