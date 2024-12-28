package com.maup.practice.facade;

import com.maup.practice.dto.BrandDTO;
import com.maup.practice.dto.CategoryDTO;
import com.maup.practice.dto.ProductDTO;
import com.maup.practice.dto.SupplierDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public interface ProductFacade {
    Page<ProductDTO> findProductsByFilters(BigDecimal minPrice, BigDecimal maxPrice, List<Long> brandIds, List<Long> supplierIds,
                                           List<Long> categoryIds, String name, Pageable pageable);

    List<CategoryDTO> findAllCategories();

    List<BrandDTO> findAllBrands();

    List<SupplierDTO> findAllSuppliers();

    ProductDTO findProductById(Long id);

    void createProduct(ProductDTO productDTO);

    void updateProduct(ProductDTO productDTO);

    void deleteProduct(Long id);

    boolean isProductInUse(Long id);
}
