package com.maup.practice.controller.rest;

import com.maup.practice.dto.ProductDTO;
import com.maup.practice.facade.ProductFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    @Autowired
    private ProductFacade productFacade;

    @GetMapping("/search")
    public ResponseEntity<PagedModel<EntityModel<ProductDTO>>> searchProducts(
            @RequestParam(required = false) BigDecimal minPrice,
            @RequestParam(required = false) BigDecimal maxPrice,
            @RequestParam(required = false) Long brandId,
            @RequestParam(required = false) Long supplierId,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) String name,
            Pageable pageable,
            PagedResourcesAssembler<ProductDTO> assembler
    ) {
        Page<ProductDTO> productsPage = productFacade.findProductsByFilters(
                minPrice, maxPrice, brandId, supplierId, categoryId, name, pageable
        );

        return ResponseEntity.ok(assembler.toModel(productsPage));
    }
}
