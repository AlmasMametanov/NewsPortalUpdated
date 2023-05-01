package com.newsPortal.NewsPortalUpdated.services.impl;

import com.newsPortal.NewsPortalUpdated.models.Category;
import com.newsPortal.NewsPortalUpdated.repositories.CategoryRepository;
import com.newsPortal.NewsPortalUpdated.services.CategoryService;
import com.newsPortal.NewsPortalUpdated.util.CategoryNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;

    @Autowired
    public CategoryServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    @Transactional
    @CacheEvict(value = "categories", key = "'categories'")
    public void createCategory(Category category) {
        categoryRepository.save(category);
    }

    @Override
    @Cacheable(value = "categories", key = "'categories'")
    public List<Category> findAllCategory(Long parentCategoryId) {
        List<Category> categoryList = categoryRepository.findAllByParentCategoryId(parentCategoryId);
        if (!categoryList.isEmpty())
            return categoryList;
        else
            throw new CategoryNotFoundException("Categories not found");
    }

    @Override
    @Transactional
    @CacheEvict(value = "categories", key = "'categories'")
    public void updateCategory(Category updatedCategory) {
        Optional<Category> category = categoryRepository.findById(updatedCategory.getId());
        if (category.isPresent()) {
            category.get().setCategoryName(updatedCategory.getCategoryName());
            category.get().setParentCategory(updatedCategory.getParentCategory());
        } else {
            throw new CategoryNotFoundException("Category not found");
        }
    }

    @Override
    @Transactional
    @CacheEvict(value = "categories", key = "'categories'")
    public void deleteById(Long categoryId) {
        Optional<Category> category = categoryRepository.findById(categoryId);
        if (category.isPresent())
            categoryRepository.deleteById(categoryId);
        else
            throw new CategoryNotFoundException("Category not found");
    }
}
