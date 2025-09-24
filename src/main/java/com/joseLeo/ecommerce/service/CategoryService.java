package com.joseLeo.ecommerce.service;

import com.joseLeo.ecommerce.entity.Category;
import com.joseLeo.ecommerce.repository.CategoryRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class CategoryService {
    private final CategoryRepository repository;

    public CategoryService(CategoryRepository repository) {
        this.repository = repository;
    }

    public List<Category> getAllCategories() {
        return repository.findAll();
    }

    public Category getCategoryById(Long id) {
        return repository.findById(id).orElse(null);
    }

    public Category saveCategory(Category category) {
        return repository.save(category);
    }

    public void deleteCategory(Long id) {
        repository.deleteById(id);
    }
}