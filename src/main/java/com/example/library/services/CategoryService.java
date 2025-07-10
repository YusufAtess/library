package com.example.library.services;

import com.example.library.repository.BookCategoryRepository;

import com.example.library.entities.Category;
import com.example.library.repository.CategoryRepository;
import java.util.List;
import java.util.stream.Collectors;

import com.example.library.response_dtos.ResponseCategoryDto;
import com.example.library.request_dtos.RequestCategoryDto;
import org.springframework.stereotype.Service;

@Service
public class CategoryService {
    private final CategoryRepository categoryRepository;
    private final BookCategoryRepository bookCategoryRepository;

    public CategoryService(CategoryRepository categoryRepository, BookCategoryRepository bookCategoryRepository) {
        this.categoryRepository = categoryRepository;
        this.bookCategoryRepository = bookCategoryRepository;
    }

    public List<ResponseCategoryDto> getAllCategory() {
        return categoryRepository.findAll().stream().map(category -> new ResponseCategoryDto(category.getName())).collect(Collectors.toList());
    }

    public Category getCategoryById(Long id) {
        return categoryRepository.findById(id).orElse(null);

    }

    public ResponseCategoryDto createCategory(RequestCategoryDto category) {
        Category category1= new Category();
        category1.setName(category.getName());
        Category category2= categoryRepository.save(category1);
        return new ResponseCategoryDto(category2.getName());
    }


    public Category updateCategory(Long id,RequestCategoryDto category) {
        return categoryRepository.findById(id).map(category1 -> {
            category1.setName(category.getName());
            return categoryRepository.save(category1);
        }).orElse(null);

    }

    public void deleteCategoryById(Long id) {
        bookCategoryRepository.findByCategory_Id(id).forEach(bookCategory -> bookCategoryRepository.deleteById(bookCategory.getId()));
        categoryRepository.deleteById(id);
    }

}


