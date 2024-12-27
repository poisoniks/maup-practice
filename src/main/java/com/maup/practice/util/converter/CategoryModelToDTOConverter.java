package com.maup.practice.util.converter;

import com.maup.practice.dto.CategoryDTO;
import com.maup.practice.model.CategoryModel;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class CategoryModelToDTOConverter implements Converter<CategoryModel, CategoryDTO> {

    @Override
    public CategoryDTO convert(CategoryModel categoryModel) {
        if (categoryModel == null) {
            return null;
        }

        CategoryDTO categoryDTO = new CategoryDTO();
        categoryDTO.setId(categoryModel.getId());
        categoryDTO.setName(categoryModel.getName());
        return categoryDTO;
    }
}
