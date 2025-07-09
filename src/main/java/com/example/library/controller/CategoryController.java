package com.example.library.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.library.entities.Category;
import com.example.library.repository.CategoryRepository;
import java.util.List;

@RestController
@RequestMapping("/api/category")
public class CategoryController {
    private final CategoryRepository categoryRepository;

    public CategoryController(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }
    @GetMapping
    public List<Category> getAllCategory() {
        return categoryRepository.findAll();
    }
    @GetMapping("{id}")
    public ResponseEntity<Category> getCategoryById(@PathVariable Long id) {
        var category= categoryRepository.findById(id).orElse(null);
        if(category == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(category);
    }
    @PostMapping
    public Category createCategory(@RequestBody Category category) {
        return categoryRepository.save(category);
    }

    @PutMapping("{id}")
    public ResponseEntity<Category> updateCategory(@PathVariable Long id, @RequestBody Category category) {
        var category2=categoryRepository.findById(id).map(category1 -> {
            category1.setName(category.getName());
            return categoryRepository.save(category1);
        }).orElse(null);
        if(category2 == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(category2);
    }
    @DeleteMapping("{id}")
    public void deleteCategoryById(@PathVariable Long id) {
        categoryRepository.deleteById(id);
    }

}

