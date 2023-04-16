package com.newsPortal.NewsPortalUpdated.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.newsPortal.NewsPortalUpdated.models.Category;
import com.newsPortal.NewsPortalUpdated.models.User;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.sql.Timestamp;

public class ArticleDTO {
    private Long id;

    @NotEmpty(message = "Заголовок не должен быть пустым")
    @Size(min = 2, max = 150, message = "Заголовок должен быть от 2 до 150 символов")
    private String headline;

    @NotEmpty(message = "Содержание не должно быть пустым")
    @Size(min = 20, max = 3000, message = "Содержание должно быть от 20 до 3000 символов")
    private String content;
    private Timestamp createdDate;
    private Category category;
    private User user;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getHeadline() {
        return headline;
    }

    public void setHeadline(String headline) {
        this.headline = headline;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Timestamp getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Timestamp createdDate) {
        this.createdDate = createdDate;
    }

    @JsonIgnore
    public Category getCategory() {
        return category;
    }

    @JsonProperty
    public void setCategory(Category category) {
        this.category = category;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
