package com.nandlal.ecommerce.service;

import com.nandlal.ecommerce.model.Category;
import com.nandlal.ecommerce.payload.CategoryDTO;
import com.nandlal.ecommerce.payload.CategoryResponse;

import java.util.List;

public interface CategoryService {

    CategoryResponse getAllCategories(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder);

    CategoryDTO createCategory(CategoryDTO categoryDTO);

    CategoryDTO updateCategory(Long categoryId, CategoryDTO categoryDTO);

    CategoryDTO deleteCategory(Long categoryId);
}
