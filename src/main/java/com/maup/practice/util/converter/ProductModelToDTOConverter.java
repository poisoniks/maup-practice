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

    private final Converter<BrandModel, BrandDTO> brandModelToDTOConverter;
    private final Converter<CategoryModel, CategoryDTO> categoryModelToDTOConverter;
    private final Converter<SupplierModel, SupplierDTO> supplierModelToDTOConverter;

    @Autowired
    public ProductModelToDTOConverter(Converter<BrandModel, BrandDTO> brandModelToDTOConverter, Converter<CategoryModel, CategoryDTO> categoryModelToDTOConverter, Converter<SupplierModel, SupplierDTO> supplierModelToDTOConverter) {
        this.brandModelToDTOConverter = brandModelToDTOConverter;
        this.categoryModelToDTOConverter = categoryModelToDTOConverter;
        this.supplierModelToDTOConverter = supplierModelToDTOConverter;
    }

    @Override
    public ProductDTO convert(ProductModel source) {
        if (source == null) {
            return null;
        }

        ProductDTO productDTO = new ProductDTO();
        productDTO.setId(source.getId());
        productDTO.setName(source.getName());
        productDTO.setPrice(source.getPrice().doubleValue());
        productDTO.setDescription(source.getDescription());
        productDTO.setStockQuantity(source.getStockQuantity());
        productDTO.setBrand(brandModelToDTOConverter.convert(source.getBrand()));
        productDTO.setCategory(categoryModelToDTOConverter.convert(source.getCategory()));
        productDTO.setSupplier(supplierModelToDTOConverter.convert(source.getSupplier()));
        return productDTO;
    }
}
