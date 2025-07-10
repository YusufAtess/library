package com.example.library.controller;


import com.example.library.services.CategoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import com.example.library.response_dtos.ResponseCategoryDto;
import com.example.library.request_dtos.RequestCategoryDto;

@RestController
@RequestMapping("/api/category")
public class CategoryController {
    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
       this.categoryService = categoryService;
    }
    @GetMapping
    public List<ResponseCategoryDto> getAllCategory() {
        return categoryService.getAllCategory();
    }
    @GetMapping("{id}")
    public ResponseEntity<ResponseCategoryDto> getCategoryById(@PathVariable Long id) {
        var category= categoryService.getCategoryById(id);
        if(category == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(new ResponseCategoryDto(category.getName()));
    }
    @PostMapping
    public ResponseCategoryDto createCategory(@RequestBody RequestCategoryDto category) {
       return categoryService.createCategory(category);
    }

    @PutMapping("{id}")
    public ResponseEntity<ResponseCategoryDto> updateCategory(@PathVariable Long id, @RequestBody RequestCategoryDto category) {
        var category2=categoryService.updateCategory(id, category);
        if(category2 == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(new ResponseCategoryDto(category2.getName()));
    }
    @DeleteMapping("{id}")
    public void deleteCategoryById(@PathVariable Long id) {
       categoryService.deleteCategoryById(id);
    }

}

