package com.newsPortal.NewsPortalUpdated.services;

import com.newsPortal.NewsPortalUpdated.models.Category;

import java.util.List;

public interface CategoryService {
    void createCategory(Category category);
    List<Category> findAllCategory(Long parentCategoryId);
    void updateCategory(Category updatedCategory);
    void deleteById(Long categoryId);
}
