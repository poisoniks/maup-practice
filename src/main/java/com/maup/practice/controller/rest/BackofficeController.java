package com.maup.practice.controller.rest;

import com.maup.practice.dto.BrandDTO;
import com.maup.practice.dto.CategoryDTO;
import com.maup.practice.dto.ProductDTO;
import com.maup.practice.dto.SupplierDTO;
import com.maup.practice.facade.ProductFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/backoffice")
public class BackofficeController {

    @Autowired
    private ProductFacade productFacade;

    @GetMapping("/selectProducts")
    public ResponseEntity<PagedModel<EntityModel<ProductDTO>>> selectProducts(
            @RequestParam(required = false) String name,
            Pageable pageable,
            PagedResourcesAssembler<ProductDTO> assembler
    ) {
        Page<ProductDTO> productsPage = productFacade.findProductsByFilters(
                null, null, null, null, null, name, pageable
        );

        return ResponseEntity.ok(assembler.toModel(productsPage));
    }

    @GetMapping("/selectCategories")
    public ResponseEntity<List<CategoryDTO>> selectCategories() {
        return ResponseEntity.ok(productFacade.findAllCategories());
    }

    @GetMapping("/selectBrands")
    public ResponseEntity<List<BrandDTO>> selectBrands() {
        return ResponseEntity.ok(productFacade.findAllBrands());
    }

    @GetMapping("/selectSuppliers")
    public ResponseEntity<List<SupplierDTO>> selectSuppliers() {
        return ResponseEntity.ok(productFacade.findAllSuppliers());
    }

    @PostMapping("/createProduct")
    public ResponseEntity<Void> createProduct(@RequestBody ProductDTO productDTO) {
        productFacade.createProduct(productDTO);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/updateProduct")
    public ResponseEntity<Void> updateProduct(@RequestBody ProductDTO productDTO) {
        productFacade.updateProduct(productDTO);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/deleteProduct")
    public ResponseEntity<Void> deleteProduct(@RequestParam Long id) {
        if (productFacade.isProductInUse(id)) {
            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .build();
        }
        productFacade.deleteProduct(id);
        return ResponseEntity.ok().build();
    }

}
