package com.maup.practice.facade.impl;

import com.maup.practice.dto.BrandDTO;
import com.maup.practice.dto.CategoryDTO;
import com.maup.practice.dto.ProductDTO;
import com.maup.practice.dto.SupplierDTO;
import com.maup.practice.facade.ProductFacade;
import com.maup.practice.model.BrandModel;
import com.maup.practice.model.CategoryModel;
import com.maup.practice.model.ProductModel;
import com.maup.practice.model.SupplierModel;
import com.maup.practice.service.BrandService;
import com.maup.practice.service.CategoryService;
import com.maup.practice.service.ProductService;
import com.maup.practice.service.SupplierService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class ProductFacadeImpl implements ProductFacade {

    @Autowired
    private ProductService productService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private BrandService brandService;

    @Autowired
    private SupplierService supplierService;

    @Autowired
    private Converter<ProductModel, ProductDTO> productModelConverter;

    @Autowired
    private Converter<CategoryModel, CategoryDTO> categoryModelConverter;

    @Autowired
    private Converter<BrandModel, BrandDTO> brandModelConverter;

    @Autowired
    private Converter<SupplierModel, SupplierDTO> supplierModelConverter;

    @Override
    public Page<ProductDTO> findProductsByFilters(BigDecimal minPrice, BigDecimal maxPrice, List<Long> brandIds, List<Long> supplierIds,
                                                  List<Long> categoryIds, String name, Pageable pageable) {
        return productService.findProductsByFilters(minPrice, maxPrice, brandIds, supplierIds, categoryIds, name, pageable)
                .map(productModelConverter::convert);
    }

    @Override
    public List<CategoryDTO> findAllCategories() {
        return categoryService.findAllCategories().stream()
                .map(categoryModelConverter::convert)
                .toList();
    }

    @Override
    public List<BrandDTO> findAllBrands() {
        return brandService.findAllBrands().stream()
                .map(brandModelConverter::convert)
                .toList();
    }

    @Override
    public List<SupplierDTO> findAllSuppliers() {
        return supplierService.findAllSuppliers().stream()
                .map(supplierModelConverter::convert)
                .toList();
    }

    @Override
    public ProductDTO findProductById(Long id) {
        return productModelConverter.convert(productService.getProductById(id));
    }
}
