package com.maup.practice.facade.impl;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.maup.practice.dto.BrandDTO;
import com.maup.practice.dto.CategoryDTO;
import com.maup.practice.dto.ProductDTO;
import com.maup.practice.dto.SupplierDTO;
import com.maup.practice.model.BrandModel;
import com.maup.practice.model.CategoryModel;
import com.maup.practice.model.ProductModel;
import com.maup.practice.model.SupplierModel;
import com.maup.practice.service.BrandService;
import com.maup.practice.service.CategoryService;
import com.maup.practice.service.ProductService;
import com.maup.practice.service.SupplierService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

@ExtendWith(MockitoExtension.class)
public class ProductFacadeImplTest {

    @Mock
    private ProductService productService;

    @Mock
    private CategoryService categoryService;

    @Mock
    private BrandService brandService;

    @Mock
    private SupplierService supplierService;

    @Mock
    private Converter<ProductModel, ProductDTO> productModelConverter;

    @Mock
    private Converter<CategoryModel, CategoryDTO> categoryModelConverter;

    @Mock
    private Converter<BrandModel, BrandDTO> brandModelConverter;

    @Mock
    private Converter<SupplierModel, SupplierDTO> supplierModelConverter;

    private ProductFacadeImpl productFacade;

    @BeforeEach
    public void setUp() {
        productFacade = new ProductFacadeImpl(productService, categoryService, brandService, supplierService,
                productModelConverter, categoryModelConverter, brandModelConverter, supplierModelConverter);
    }

    @Test
    void shouldFindProductsByFiltersWhenValidFiltersProvided() {
        BigDecimal minPrice = BigDecimal.valueOf(50);
        BigDecimal maxPrice = BigDecimal.valueOf(200);
        List<Long> brandIds = Arrays.asList(1L, 2L);
        List<Long> supplierIds = List.of(3L);
        List<Long> categoryIds = Arrays.asList(4L, 5L);
        String name = "Test Product";
        Pageable pageable = mock(Pageable.class);

        ProductDTO dto1 = new ProductDTO();
        dto1.setId(10L);
        ProductDTO dto2 = new ProductDTO();
        dto2.setId(11L);

        Page<ProductModel> productPage = mock(Page.class);

        when(productPage.map(Mockito.any())).thenReturn(new PageImpl<>(Arrays.asList(dto1, dto2)));
        when(productService.findProductsByFilters(minPrice, maxPrice, brandIds, supplierIds, categoryIds, name, pageable)).thenReturn(productPage);

        Page<ProductDTO> result = productFacade.findProductsByFilters(minPrice, maxPrice, brandIds, supplierIds, categoryIds, name, pageable);

        verify(productService, times(1)).findProductsByFilters(minPrice, maxPrice, brandIds, supplierIds, categoryIds, name, pageable);
        verify(productPage, times(1)).map(Mockito.any());
        assertEquals(2, result.getContent().size());
        assertTrue(result.getContent().contains(dto1));
        assertTrue(result.getContent().contains(dto2));
    }

    @Test
    void shouldFindAllCategoriesWhenCalled() {
        CategoryModel category1 = new CategoryModel();
        category1.setId(20L);
        CategoryModel category2 = new CategoryModel();
        category2.setId(21L);

        CategoryDTO dto1 = new CategoryDTO();
        dto1.setId(20L);
        CategoryDTO dto2 = new CategoryDTO();
        dto2.setId(21L);

        List<CategoryModel> categories = Arrays.asList(category1, category2);

        when(categoryService.findAllCategories()).thenReturn(categories);
        when(categoryModelConverter.convert(category1)).thenReturn(dto1);
        when(categoryModelConverter.convert(category2)).thenReturn(dto2);

        List<CategoryDTO> result = productFacade.findAllCategories();

        verify(categoryService, times(1)).findAllCategories();
        verify(categoryModelConverter, times(1)).convert(category1);
        verify(categoryModelConverter, times(1)).convert(category2);
        assertEquals(2, result.size());
        assertTrue(result.contains(dto1));
        assertTrue(result.contains(dto2));
    }

    @Test
    void shouldFindAllBrandsWhenCalled() {
        BrandModel brand1 = new BrandModel();
        brand1.setId(30L);
        BrandModel brand2 = new BrandModel();
        brand2.setId(31L);

        BrandDTO dto1 = new BrandDTO();
        dto1.setId(30L);
        BrandDTO dto2 = new BrandDTO();
        dto2.setId(31L);

        List<BrandModel> brands = Arrays.asList(brand1, brand2);

        when(brandService.findAllBrands()).thenReturn(brands);
        when(brandModelConverter.convert(brand1)).thenReturn(dto1);
        when(brandModelConverter.convert(brand2)).thenReturn(dto2);

        List<BrandDTO> result = productFacade.findAllBrands();

        verify(brandService, times(1)).findAllBrands();
        verify(brandModelConverter, times(1)).convert(brand1);
        verify(brandModelConverter, times(1)).convert(brand2);
        assertEquals(2, result.size());
        assertTrue(result.contains(dto1));
        assertTrue(result.contains(dto2));
    }

    @Test
    void shouldFindAllSuppliersWhenCalled() {
        SupplierModel supplier1 = new SupplierModel();
        supplier1.setId(40L);
        SupplierModel supplier2 = new SupplierModel();
        supplier2.setId(41L);

        SupplierDTO dto1 = new SupplierDTO();
        dto1.setId(40L);
        SupplierDTO dto2 = new SupplierDTO();
        dto2.setId(41L);

        List<SupplierModel> suppliers = Arrays.asList(supplier1, supplier2);

        when(supplierService.findAllSuppliers()).thenReturn(suppliers);
        when(supplierModelConverter.convert(supplier1)).thenReturn(dto1);
        when(supplierModelConverter.convert(supplier2)).thenReturn(dto2);

        List<SupplierDTO> result = productFacade.findAllSuppliers();

        verify(supplierService, times(1)).findAllSuppliers();
        verify(supplierModelConverter, times(1)).convert(supplier1);
        verify(supplierModelConverter, times(1)).convert(supplier2);
        assertEquals(2, result.size());
        assertTrue(result.contains(dto1));
        assertTrue(result.contains(dto2));
    }

    @Test
    void shouldFindProductByIdWhenProductExists() {
        Long productId = 50L;
        ProductModel product = new ProductModel();
        product.setId(productId);

        ProductDTO dto = new ProductDTO();
        dto.setId(productId);

        when(productService.getProductById(productId)).thenReturn(product);
        when(productModelConverter.convert(product)).thenReturn(dto);

        ProductDTO result = productFacade.findProductById(productId);

        verify(productService, times(1)).getProductById(productId);
        verify(productModelConverter, times(1)).convert(product);
        assertEquals(dto, result);
    }

    @Test
    void shouldFindProductByIdWhenProductDoesNotExist() {
        Long productId = 60L;

        when(productService.getProductById(productId)).thenReturn(null);

        ProductDTO result = productFacade.findProductById(productId);

        verify(productService, times(1)).getProductById(productId);
        verify(productModelConverter, times(1)).convert(null);
        assertNull(result);
    }

    @Test
    void shouldCreateProductWhenValidProductDTO() {
        ProductDTO productDTO = new ProductDTO();
        productDTO.setName("New Product");
        productDTO.setPrice(99.99);
        productDTO.setDescription("Description");
        productDTO.setStockQuantity(50);
        BrandDTO brandDTO = new BrandDTO();
        brandDTO.setId(70L);
        CategoryDTO categoryDTO = new CategoryDTO();
        categoryDTO.setId(80L);
        SupplierDTO supplierDTO = new SupplierDTO();
        supplierDTO.setId(90L);
        productDTO.setBrand(brandDTO);
        productDTO.setCategory(categoryDTO);
        productDTO.setSupplier(supplierDTO);

        BrandModel brandModel = new BrandModel();
        brandModel.setId(70L);
        CategoryModel categoryModel = new CategoryModel();
        categoryModel.setId(80L);
        SupplierModel supplierModel = new SupplierModel();
        supplierModel.setId(90L);

        when(brandService.findBrandById(70L)).thenReturn(brandModel);
        when(categoryService.findCategoryById(80L)).thenReturn(categoryModel);
        when(supplierService.findSupplierById(90L)).thenReturn(supplierModel);

        productFacade.createProduct(productDTO);

        ArgumentCaptor<ProductModel> productModelCaptor = ArgumentCaptor.forClass(ProductModel.class);
        verify(productService, times(1)).saveProduct(productModelCaptor.capture());

        ProductModel savedProduct = productModelCaptor.getValue();
        assertEquals("New Product", savedProduct.getName());
        assertEquals(BigDecimal.valueOf(99.99), savedProduct.getPrice());
        assertEquals("Description", savedProduct.getDescription());
        assertEquals(50, savedProduct.getStockQuantity());
        assertEquals(brandModel, savedProduct.getBrand());
        assertEquals(categoryModel, savedProduct.getCategory());
        assertEquals(supplierModel, savedProduct.getSupplier());
    }

    @Test
    void shouldUpdateProductWhenProductExists() {
        ProductDTO productDTO = new ProductDTO();
        productDTO.setId(100L);
        productDTO.setName("Updated Product");
        productDTO.setPrice(149.99);
        productDTO.setDescription("Updated Description");
        productDTO.setStockQuantity(30);
        BrandDTO brandDTO = new BrandDTO();
        brandDTO.setId(71L);
        CategoryDTO categoryDTO = new CategoryDTO();
        categoryDTO.setId(81L);
        SupplierDTO supplierDTO = new SupplierDTO();
        supplierDTO.setId(91L);
        productDTO.setBrand(brandDTO);
        productDTO.setCategory(categoryDTO);
        productDTO.setSupplier(supplierDTO);

        ProductModel existingProduct = new ProductModel();
        existingProduct.setId(100L);

        BrandModel brandModel = new BrandModel();
        brandModel.setId(71L);
        CategoryModel categoryModel = new CategoryModel();
        categoryModel.setId(81L);
        SupplierModel supplierModel = new SupplierModel();
        supplierModel.setId(91L);

        when(productService.getProductById(100L)).thenReturn(existingProduct);
        when(brandService.findBrandById(71L)).thenReturn(brandModel);
        when(categoryService.findCategoryById(81L)).thenReturn(categoryModel);
        when(supplierService.findSupplierById(91L)).thenReturn(supplierModel);

        productFacade.updateProduct(productDTO);

        ArgumentCaptor<ProductModel> productModelCaptor = ArgumentCaptor.forClass(ProductModel.class);
        verify(productService, times(1)).saveProduct(productModelCaptor.capture());

        ProductModel updatedProduct = productModelCaptor.getValue();
        assertEquals("Updated Product", updatedProduct.getName());
        assertEquals(BigDecimal.valueOf(149.99), updatedProduct.getPrice());
        assertEquals("Updated Description", updatedProduct.getDescription());
        assertEquals(30, updatedProduct.getStockQuantity());
        assertEquals(brandModel, updatedProduct.getBrand());
        assertEquals(categoryModel, updatedProduct.getCategory());
        assertEquals(supplierModel, updatedProduct.getSupplier());
    }

    @Test
    void shouldThrowIllegalArgumentExceptionWhenUpdateProductAndProductDoesNotExist() {
        ProductDTO productDTO = new ProductDTO();
        productDTO.setId(110L);

        when(productService.getProductById(110L)).thenReturn(null);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            productFacade.updateProduct(productDTO);
        });

        assertEquals("Product with id 110 not found", exception.getMessage());
        verify(productService, times(1)).getProductById(110L);
        verify(productService, never()).saveProduct(any());
    }

    @Test
    void shouldDeleteProductWhenProductIdExists() {
        Long productId = 120L;

        productFacade.deleteProduct(productId);

        verify(productService, times(1)).deleteProduct(productId);
    }

    @Test
    void shouldIsProductInUseWhenProductIdIsProvided() {
        Long productId = 130L;

        when(productService.isProductInUse(productId)).thenReturn(true);

        boolean result = productFacade.isProductInUse(productId);

        verify(productService, times(1)).isProductInUse(productId);
        assertTrue(result);
    }
}
