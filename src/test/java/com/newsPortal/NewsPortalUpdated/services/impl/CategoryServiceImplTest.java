package com.newsPortal.NewsPortalUpdated.services.impl;

import com.newsPortal.NewsPortalUpdated.models.Category;
import com.newsPortal.NewsPortalUpdated.repositories.CategoryRepository;
import com.newsPortal.NewsPortalUpdated.util.CategoryNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CategoryServiceImplTest {
    @InjectMocks
    private CategoryServiceImpl categoryService;

    @Mock
    private CategoryRepository categoryRepository;

    @Test
    void canCreateCategory() {
        // given
        Category category = new Category();
        category.setCategoryName("Category");
        // when
        categoryService.createCategory(category);
        // then
        ArgumentCaptor<Category> categoryArgumentCaptor = ArgumentCaptor.forClass(Category.class);
        verify(categoryRepository).save(categoryArgumentCaptor.capture());

        Category savedCategory = categoryArgumentCaptor.getValue();
        assertEquals(category.getCategoryName(), savedCategory.getCategoryName());
    }

    @Test
    void willThrowExceptionCategoryNotFoundWhenFindAllCategory() {
        // given
        Long parentCategoryId = null;
        List<Category> categoryList = new ArrayList<>();
        when(categoryRepository.findAllByParentCategoryId(parentCategoryId)).thenReturn(categoryList);
        // when
        // then
        assertThatThrownBy(() -> categoryService.findAllCategory(parentCategoryId))
                .isInstanceOf(CategoryNotFoundException.class)
                .hasMessageContaining("Categories not found");
    }

    @Test
    void canFindAllCategory() {
        // given
        Long parentCategoryId = null;
        Category category1 = new Category("Category1");
        Category category2 = new Category("Category2");
        List<Category> categoryList = List.of(category1, category2);
        when(categoryRepository.findAllByParentCategoryId(parentCategoryId)).thenReturn(categoryList);
        // when
        List<Category> result = categoryService.findAllCategory(parentCategoryId);
        // then
        assertEquals(categoryList.size(), result.size());
        assertTrue(result.contains(category1));
        assertTrue(result.contains(category2));
    }

    @Test
    void willThrowExceptionCategoryNotFoundWhenUpdateCategory() {
        // given
        Category updatedCategory = new Category(1L, "Updated category", new Category("Updated parent category"));
        when(categoryRepository.findById(updatedCategory.getId())).thenReturn(Optional.empty());
        // when
        // then
        assertThatThrownBy(() -> categoryService.updateCategory(updatedCategory))
                .isInstanceOf(CategoryNotFoundException.class)
                .hasMessageContaining("Category not found");
    }

    @Test
    void canUpdateCategory() {
        // given
        Category updatedCategory = new Category(1L, "Updated category", new Category("Updated parent category"));
        Category existedCategory = new Category(1L, "Category", new Category("Parent category"));
        when(categoryRepository.findById(updatedCategory.getId())).thenReturn(Optional.of(existedCategory));
        // when
        categoryService.updateCategory(updatedCategory);
        // then
        assertEquals(updatedCategory.getCategoryName(), existedCategory.getCategoryName());
        assertEquals(updatedCategory.getParentCategory(), existedCategory.getParentCategory());
        assertEquals(updatedCategory.getId(), existedCategory.getId());
    }

    @Test
    void willThrowExceptionCategoryNotFoundWhenDeleteCategory() {
        // given
        Long categoryId = 1L;
        when(categoryRepository.findById(categoryId)).thenReturn(Optional.empty());
        // when
        // then
        assertThatThrownBy(() -> categoryService.deleteById(categoryId))
                .isInstanceOf(CategoryNotFoundException.class)
                .hasMessageContaining("Category not found");
    }

    @Test
    void canDeleteCategory() {
        // given
        Long categoryId = 1L;
        Category category = new Category();
        when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(category));
        // when
        categoryService.deleteById(categoryId);
        // then
        verify(categoryRepository).deleteById(categoryId);
    }
}
