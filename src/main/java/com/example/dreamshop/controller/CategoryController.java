package com.example.dreamshop.controller;


import com.example.dreamshop.exceptions.AlreadyExistsException;
import com.example.dreamshop.exceptions.ResourceNotFoundException;
import com.example.dreamshop.model.Category;
import com.example.dreamshop.response.ApiResponse;
import com.example.dreamshop.service.category.ICategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("${api.prefix}/categories")
public class CategoryController {
    private final ICategoryService categoryService;


        @GetMapping("/all")
        public ResponseEntity<ApiResponse> getAllCategories() {
            try {
                List<Category> categories = categoryService.getAllCategories();
                return ResponseEntity.ok(new ApiResponse("Found!", categories));
            } catch (Exception e) {
                return ResponseEntity.status(INTERNAL_SERVER_ERROR).body( new ApiResponse("Error:",INTERNAL_SERVER_ERROR));
            }
        }

        @PostMapping("/add")
        public ResponseEntity<ApiResponse> addCategory(@RequestBody Category name) {
            try {
                Category theCategory = categoryService.addCategory(name);
                return ResponseEntity.ok(new ApiResponse("Added Category", theCategory));
            } catch (AlreadyExistsException e) {
            return ResponseEntity.status(CONFLICT).body(new ApiResponse(e.getMessage(),null));
            }
        }

        @GetMapping("/category/{id}/category")
        public ResponseEntity<ApiResponse> getCategoryById(@PathVariable Long id) {
            try {
                Category theCategory = categoryService.getCategoryById(id);
                return ResponseEntity.ok(new ApiResponse("Found Category", theCategory));
            } catch (ResourceNotFoundException e) {
                return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(),null));
            }
        }

    @GetMapping("/category/{name}/category")
    public ResponseEntity<ApiResponse> getCategoryByName(@PathVariable String name) {
        try {
            Category theCategory = categoryService.getCategoryByName(name);
            return ResponseEntity.ok(new ApiResponse("Found Category", theCategory));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(),null));
        }
    }

    @DeleteMapping("/category/{id}/delete")
    public ResponseEntity<ApiResponse> deleteCategory(@PathVariable Long id) {
        try {
            categoryService.deleteCategoryById(id);
            return ResponseEntity.ok(new ApiResponse("Delete Category", null));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(),null));
        }
    }
    @PutMapping("/category/{id}/update")
    public ResponseEntity<ApiResponse> deleteCategory(@PathVariable Long id, @RequestBody Category category) {
        try {
            Category updateCategory = categoryService.updateCategory(category, id);
            return ResponseEntity.ok(new ApiResponse("Update success!", updateCategory));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(),null));
        }
    }

 }
