package com.joseLeo.ecommerce.controller;

import com.joseLeo.ecommerce.entity.Category;
import com.joseLeo.ecommerce.entity.Product;
import com.joseLeo.ecommerce.service.CategoryService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/categories")
public class CategoryController {

    private final CategoryService service;

    public CategoryController(CategoryService service) {
        this.service = service;
    }

    @GetMapping
    public List<Category> getAll() {
        return service.getAllCategories();
    }

    @PostMapping
    public Category save(@RequestBody Category category) {
        return service.saveCategory(category);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.deleteCategory(id);
    }


    // PUT para actualizar producto
    @PutMapping("/{id}")
    public Category updateCategory(@PathVariable Long id, @RequestBody Category categoryDetails) {
        Category category = CategoryService.getCategoryById(id);
        if (category == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Categoria no encontrado");
        }

        category.setName(categoryDetails.getName());


        return CategoryService.saveCategory(category);
    }
}