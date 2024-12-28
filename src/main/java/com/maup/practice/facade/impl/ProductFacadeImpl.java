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

    private final ProductService productService;
    private final CategoryService categoryService;
    private final BrandService brandService;
    private final SupplierService supplierService;
    private final Converter<ProductModel, ProductDTO> productModelConverter;
    private final Converter<CategoryModel, CategoryDTO> categoryModelConverter;
    private final Converter<BrandModel, BrandDTO> brandModelConverter;
    private final Converter<SupplierModel, SupplierDTO> supplierModelConverter;

    @Autowired
    public ProductFacadeImpl(ProductService productService, CategoryService categoryService, BrandService brandService,
                             SupplierService supplierService, Converter<ProductModel, ProductDTO> productModelConverter,
                             Converter<CategoryModel, CategoryDTO> categoryModelConverter, Converter<BrandModel, BrandDTO> brandModelConverter,
                             Converter<SupplierModel, SupplierDTO> supplierModelConverter) {
        this.productService = productService;
        this.categoryService = categoryService;
        this.brandService = brandService;
        this.supplierService = supplierService;
        this.productModelConverter = productModelConverter;
        this.categoryModelConverter = categoryModelConverter;
        this.brandModelConverter = brandModelConverter;
        this.supplierModelConverter = supplierModelConverter;
    }

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

    @Override
    public void createProduct(ProductDTO productDTO) {
        ProductModel productModel = new ProductModel();
        updateProductModel(productModel, productDTO);
        productService.saveProduct(productModel);
    }

    @Override
    public void updateProduct(ProductDTO productDTO) {
        ProductModel productModel = productService.getProductById(productDTO.getId());
        if (productModel == null) {
            throw new IllegalArgumentException("Product with id " + productDTO.getId() + " not found");
        }
        updateProductModel(productModel, productDTO);
        productService.saveProduct(productModel);
    }

    @Override
    public void deleteProduct(Long id) {
        productService.deleteProduct(id);
    }

    @Override
    public boolean isProductInUse(Long id) {
        return productService.isProductInUse(id);
    }

    private void updateProductModel(ProductModel productModel, ProductDTO productDTO) {
        productModel.setName(productDTO.getName());
        productModel.setPrice(BigDecimal.valueOf(productDTO.getPrice()));
        productModel.setDescription(productDTO.getDescription());
        productModel.setStockQuantity(productDTO.getStockQuantity());
        productModel.setBrand(brandService.findBrandById(productDTO.getBrand().getId()));
        productModel.setCategory(categoryService.findCategoryById(productDTO.getCategory().getId()));
        productModel.setSupplier(supplierService.findSupplierById(productDTO.getSupplier().getId()));
    }
}
