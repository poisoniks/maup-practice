package com.maup.practice.facade.impl;

import com.maup.practice.dto.ProductDTO;
import com.maup.practice.facade.ProductFacade;
import com.maup.practice.model.ProductModel;
import com.maup.practice.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class ProductFacadeImpl implements ProductFacade {

    @Autowired
    private ProductService productService;

    @Autowired
    private Converter<ProductModel, ProductDTO> productModelConverter;

    @Override
    public Page<ProductDTO> findProductsByFilters(BigDecimal minPrice, BigDecimal maxPrice, Long brandId, Long supplierId, Long categoryId, String name, Pageable pageable) {
        return productService.findProductsByFilters(minPrice, maxPrice, brandId, supplierId, categoryId, name, pageable)
                .map(productModelConverter::convert);
    }
}
