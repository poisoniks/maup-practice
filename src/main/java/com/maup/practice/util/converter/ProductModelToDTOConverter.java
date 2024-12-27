package com.maup.practice.util.converter;

import com.maup.practice.dto.BrandDTO;
import com.maup.practice.dto.CategoryDTO;
import com.maup.practice.dto.ProductDTO;
import com.maup.practice.dto.SupplierDTO;
import com.maup.practice.model.BrandModel;
import com.maup.practice.model.CategoryModel;
import com.maup.practice.model.ProductModel;
import com.maup.practice.model.SupplierModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class ProductModelToDTOConverter implements Converter<ProductModel, ProductDTO> {

    @Autowired
    private Converter<BrandModel, BrandDTO> brandModelToDTOConverter;

    @Autowired
    private Converter<CategoryModel, CategoryDTO> categoryModelToDTOConverter;

    @Autowired
    private Converter<SupplierModel, SupplierDTO> supplierModelToDTOConverter;

    @Override
    public ProductDTO convert(ProductModel productModel) {
        if (productModel == null) {
            return null;
        }

        ProductDTO productDTO = new ProductDTO();
        productDTO.setId(productModel.getId());
        productDTO.setName(productModel.getName());
        productDTO.setPrice(productModel.getPrice().doubleValue());
        productDTO.setDescription(productModel.getDescription());
        productDTO.setStockQuantity(productModel.getStockQuantity());
        productDTO.setBrand(brandModelToDTOConverter.convert(productModel.getBrand()));
        productDTO.setCategory(categoryModelToDTOConverter.convert(productModel.getCategory()));
        productDTO.setSupplier(supplierModelToDTOConverter.convert(productModel.getSupplier()));
        return productDTO;
    }
}
