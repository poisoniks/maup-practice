package com.maup.practice.facade;

import com.maup.practice.dto.ProductDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public interface ProductFacade {
    Page<ProductDTO> findProductsByFilters(BigDecimal minPrice, BigDecimal maxPrice, Long brandId, Long supplierId,
                                           Long categoryId, String name, Pageable pageable);
}
