package com.maup.practice.service.impl;

import com.maup.practice.model.CategoryModel;
import com.maup.practice.repository.CategoryRepository;
import com.maup.practice.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    public List<CategoryModel> findAllCategories() {
        return categoryRepository.findAll();
    }

    @Override
    public CategoryModel findCategoryById(Long id) {
        return categoryRepository.findById(id).orElseThrow(() -> new RuntimeException("Category not found"));
    }
}
