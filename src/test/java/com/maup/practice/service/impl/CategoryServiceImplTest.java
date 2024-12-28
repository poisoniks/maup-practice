package com.maup.practice.service.impl;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.maup.practice.model.CategoryModel;
import com.maup.practice.repository.CategoryRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class CategoryServiceImplTest {

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private CategoryServiceImpl categoryService;

    @Test
    void shouldReturnAllCategoriesWhenCategoriesExist() {
        CategoryModel category1 = new CategoryModel();
        category1.setId(1L);
        category1.setName("Electronics");

        CategoryModel category2 = new CategoryModel();
        category2.setId(2L);
        category2.setName("Books");

        when(categoryRepository.findAll()).thenReturn(Arrays.asList(category1, category2));

        List<CategoryModel> categories = categoryService.findAllCategories();

        verify(categoryRepository, times(1)).findAll();
        assertEquals(2, categories.size());
        assertTrue(categories.contains(category1));
        assertTrue(categories.contains(category2));
    }

    @Test
    void shouldReturnEmptyListWhenNoCategoriesExist() {
        when(categoryRepository.findAll()).thenReturn(Arrays.asList());

        List<CategoryModel> categories = categoryService.findAllCategories();

        verify(categoryRepository, times(1)).findAll();
        assertNotNull(categories);
        assertTrue(categories.isEmpty());
    }

    @Test
    void shouldReturnCategoryWhenCategoryExists() {
        Long categoryId = 3L;
        CategoryModel category = new CategoryModel();
        category.setId(categoryId);
        category.setName("Clothing");

        when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(category));

        CategoryModel foundCategory = categoryService.findCategoryById(categoryId);

        verify(categoryRepository, times(1)).findById(categoryId);
        assertEquals(category, foundCategory);
    }

    @Test
    void shouldThrowRuntimeExceptionWhenCategoryDoesNotExist() {
        Long categoryId = 4L;

        when(categoryRepository.findById(categoryId)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            categoryService.findCategoryById(categoryId);
        });

        assertEquals("Category not found", exception.getMessage());
        verify(categoryRepository, times(1)).findById(categoryId);
    }
}
