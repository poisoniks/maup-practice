package com.maup.practice.service.impl;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.maup.practice.model.BrandModel;
import com.maup.practice.repository.BrandRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class BrandServiceImplTest {

    @Mock
    private BrandRepository brandRepository;

    @InjectMocks
    private BrandServiceImpl brandService;

    @Test
    void shouldFindAllBrandsWhenBrandsExist() {
        BrandModel brand1 = new BrandModel();
        brand1.setId(1L);
        brand1.setName("Brand A");

        BrandModel brand2 = new BrandModel();
        brand2.setId(2L);
        brand2.setName("Brand B");

        when(brandRepository.findAll()).thenReturn(Arrays.asList(brand1, brand2));

        List<BrandModel> brands = brandService.findAllBrands();

        verify(brandRepository, times(1)).findAll();
        assertEquals(2, brands.size());
        assertTrue(brands.contains(brand1));
        assertTrue(brands.contains(brand2));
    }

    @Test
    void shouldFindAllBrandsWhenNoBrandsExist() {
        when(brandRepository.findAll()).thenReturn(Arrays.asList());

        List<BrandModel> brands = brandService.findAllBrands();

        verify(brandRepository, times(1)).findAll();
        assertNotNull(brands);
        assertTrue(brands.isEmpty());
    }

    @Test
    void shouldFindBrandByIdWhenBrandExists() {
        Long brandId = 3L;
        BrandModel brand = new BrandModel();
        brand.setId(brandId);
        brand.setName("Brand C");

        when(brandRepository.findById(brandId)).thenReturn(Optional.of(brand));

        BrandModel foundBrand = brandService.findBrandById(brandId);

        verify(brandRepository, times(1)).findById(brandId);
        assertEquals(brand, foundBrand);
    }

    @Test
    void shouldThrowRuntimeExceptionWhenBrandDoesNotExist() {
        Long brandId = 4L;

        when(brandRepository.findById(brandId)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            brandService.findBrandById(brandId);
        });

        assertEquals("Brand not found", exception.getMessage());
        verify(brandRepository, times(1)).findById(brandId);
    }
}
