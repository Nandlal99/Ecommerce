package com.nandlal.ecommerce.service;

import com.nandlal.ecommerce.exception.APIException;
import com.nandlal.ecommerce.exception.ResourceNotFoundException;
import com.nandlal.ecommerce.model.Category;
import com.nandlal.ecommerce.payload.CategoryDTO;
import com.nandlal.ecommerce.payload.CategoryResponse;
import com.nandlal.ecommerce.repository.CategoryRepository;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryServiceImp implements CategoryService{

    private final CategoryRepository categoryRepository;
    private final ModelMapper modelMapper;

    public CategoryServiceImp(CategoryRepository categoryRepository, ModelMapper modelMapper) {
        this.categoryRepository = categoryRepository;
        this.modelMapper = modelMapper;
    }


    @Override
    public CategoryResponse getAllCategories(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {
        Sort sortByAndOrder = sortOrder.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageDetails = PageRequest.of(pageNumber, pageSize, sortByAndOrder);
        Page<Category> categoryPage = categoryRepository.findAll(pageDetails);

        List<Category> categories = categoryPage.getContent();
        if (categories.isEmpty()){
            throw new APIException("No category created till now !!!");
        }
        List<CategoryDTO> categoryDTOS = categories.stream()
                .map(category -> modelMapper.map(category, CategoryDTO.class))
                .toList();

        CategoryResponse categoryResponse = new CategoryResponse();
        categoryResponse.setContent(categoryDTOS);
        categoryResponse.setPageNumber(categoryPage.getNumber());
        categoryResponse.setPageSize(categoryPage.getSize());
        categoryResponse.setTotalPages(categoryPage.getTotalPages());
        categoryResponse.setTotalElements(categoryPage.getTotalElements());
        categoryResponse.setLastPage(categoryPage.isLast());
        return categoryResponse;
    }

    @Override
    public CategoryDTO createCategory(CategoryDTO categoryDTO) {
        Category category = modelMapper.map(categoryDTO, Category.class);
        Category savedCategory = categoryRepository.findByCategoryName(category.getCategoryName());
        if (savedCategory != null){
            throw new APIException("Category with the name "+category.getCategoryName()+" already exist !!!");
        }
        Category category1 = categoryRepository.save(category);
        return modelMapper.map(category1, CategoryDTO.class);
    }

    @Override
    public CategoryDTO updateCategory(Long categoryId, CategoryDTO categoryDTO) {
        Optional<Category> saveCategory = categoryRepository.findById(categoryId);
        if(saveCategory.isEmpty()){
            throw new ResourceNotFoundException("Invalid category details and categoryId:"+categoryId);
        }
        Category category = modelMapper.map(categoryDTO, Category.class);
        Category savedCategoryName = categoryRepository.findByCategoryName(category.getCategoryName());
        if(savedCategoryName != null){
            throw new APIException("Category with the name "+category.getCategoryName()+" already exist !!!");
        }

        Category savedCategory = saveCategory.get();
        savedCategory.setCategoryName(category.getCategoryName());
        Category category1 = categoryRepository.save(savedCategory);
        return modelMapper.map(category1, CategoryDTO.class);
    }

    @Override
    public CategoryDTO deleteCategory(Long categoryId) {
        Optional<Category> category = categoryRepository.findById(categoryId);
        if(category.isEmpty()){
            throw new ResourceNotFoundException("Invalid categoryId:"+categoryId);
        }
        categoryRepository.deleteById(categoryId);
        return modelMapper.map(category.get(), CategoryDTO.class);
    }
}
