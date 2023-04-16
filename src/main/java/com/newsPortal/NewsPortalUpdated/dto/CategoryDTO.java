package com.newsPortal.NewsPortalUpdated.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.newsPortal.NewsPortalUpdated.models.Category;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.List;

public class CategoryDTO {
    private Long id;

    @NotEmpty(message = "Категория не должно быть пустым")
    @Size(min = 1, max = 50, message = "Категория должна быть от 1 до 50 символов")
    private String categoryName;

    private Category parentCategory;

    private List<Category> childCategoryList;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    @JsonIgnore
    public Category getParentCategory() {
        return parentCategory;
    }

    @JsonProperty
    public void setParentCategory(Category parentCategory) {
        this.parentCategory = parentCategory;
    }

    public List<Category> getChildCategoryList() {
        return childCategoryList;
    }

    public void setChildCategoryList(List<Category> childCategoryList) {
        this.childCategoryList = childCategoryList;
    }
}
