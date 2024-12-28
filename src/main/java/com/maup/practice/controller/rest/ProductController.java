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
import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductFacade productFacade;

    @Autowired
    public ProductController(ProductFacade productFacade) {
        this.productFacade = productFacade;
    }

    @GetMapping("/search")
    public ResponseEntity<PagedModel<EntityModel<ProductDTO>>> searchProducts(
            @RequestParam(required = false) BigDecimal minPrice,
            @RequestParam(required = false) BigDecimal maxPrice,
            @RequestParam(required = false) List<Long> brandIds,
            @RequestParam(required = false) List<Long> supplierIds,
            @RequestParam(required = false) List<Long> categoryIds,
            @RequestParam(required = false) String name,
            Pageable pageable,
            PagedResourcesAssembler<ProductDTO> assembler
    ) {
        Page<ProductDTO> productsPage = productFacade.findProductsByFilters(
                minPrice, maxPrice, brandIds, supplierIds, categoryIds, name, pageable
        );

        return ResponseEntity.ok(assembler.toModel(productsPage));
    }
}
