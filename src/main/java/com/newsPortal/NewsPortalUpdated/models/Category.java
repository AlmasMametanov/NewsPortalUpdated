package com.newsPortal.NewsPortalUpdated.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "category")
public class Category implements Serializable {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "category_name")
    @NotEmpty(message = "Категория не должно быть пустым")
    @Size(min = 1, max = 50, message = "Категория должна быть от 1 до 50 символов")
    private String categoryName;

    @OneToMany(mappedBy = "category")
    @JsonIgnore
    private List<Article> articleList;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id", referencedColumnName = "id")
    private Category parentCategory;

    @OneToMany(mappedBy = "parentCategory", fetch = FetchType.EAGER)
    private List<Category> childCategoryList;

    public Category() {
    }

    public Category(String categoryName, Category parentCategory) {
        this.categoryName = categoryName;
        this.parentCategory = parentCategory;
    }

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

    public List<Article> getArticleList() {
        return articleList;
    }

    public void setArticleList(List<Article> articleList) {
        this.articleList = articleList;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Category category = (Category) o;
        return Objects.equals(categoryName, category.categoryName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(categoryName);
    }

    @Override
    public String toString() {
        return "Category{" +
                "categoryName='" + categoryName + '\'' +
                '}';
    }
}
