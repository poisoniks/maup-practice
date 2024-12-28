package com.maup.practice.service;

import com.maup.practice.model.CategoryModel;

import java.util.List;

public interface CategoryService {
    List<CategoryModel> findAllCategories();
    CategoryModel findCategoryById(Long id);
}
