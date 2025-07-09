package com.example.library.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.library.entities.Category;
import com.example.library.repository.CategoryRepository;
import java.util.List;
import java.util.stream.Collectors;

import com.example.library.response_dtos.ResponseCategoryDto;
import com.example.library.request_dtos.RequestCategoryDto;

@RestController
@RequestMapping("/api/category")
public class CategoryController {
    private final CategoryRepository categoryRepository;

    public CategoryController(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }
    @GetMapping
    public List<ResponseCategoryDto> getAllCategory() {
        return categoryRepository.findAll().stream().map(category -> new ResponseCategoryDto(category.getName())).collect(Collectors.toList());
    }
    @GetMapping("{id}")
    public ResponseEntity<ResponseCategoryDto> getCategoryById(@PathVariable Long id) {
        var category= categoryRepository.findById(id).orElse(null);
        if(category == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(new ResponseCategoryDto(category.getName()));
    }
    @PostMapping
    public ResponseCategoryDto createCategory(@RequestBody RequestCategoryDto category) {
        Category category1= new Category();
        category1.setName(category.getName());
        Category category2= categoryRepository.save(category1);
        return new ResponseCategoryDto(category2.getName());
    }

    @PutMapping("{id}")
    public ResponseEntity<ResponseCategoryDto> updateCategory(@PathVariable Long id, @RequestBody RequestCategoryDto category) {
        var category2=categoryRepository.findById(id).map(category1 -> {
            category1.setName(category.getName());
            return categoryRepository.save(category1);
        }).orElse(null);
        if(category2 == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(new ResponseCategoryDto(category2.getName()));
    }
    @DeleteMapping("{id}")
    public void deleteCategoryById(@PathVariable Long id) {
        categoryRepository.deleteById(id);
    }

}

