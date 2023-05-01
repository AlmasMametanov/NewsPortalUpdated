package com.newsPortal.NewsPortalUpdated.models;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Objects;

@Entity
@Table(name = "article")
public class Article implements Serializable {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "headline")
    @NotEmpty(message = "Заголовок не должен быть пустым")
    @Size(min = 2, max = 150, message = "Заголовок должен быть от 2 до 150 символов")
    private String headline;

    @Column(name = "content")
    @NotEmpty(message = "Содержание не должно быть пустым")
    @Size(min = 20, max = 3000, message = "Содержание должно быть от 20 до 3000 символов")
    private String content;

    @Column(name = "created_date")
    private Timestamp createdDate;

    @Column(name = "updated_date")
    private Timestamp updatedDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", referencedColumnName = "id")
    private Category category;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    public Article() {
    }

    public Article(String headline, String content, Timestamp createdDate, Timestamp updatedDate) {
        this.headline = headline;
        this.content = content;
        this.createdDate = createdDate;
        this.updatedDate = updatedDate;
    }

    public Article(Long id, String headline, String content, Timestamp updatedDate, Category category) {
        this.id = id;
        this.headline = headline;
        this.content = content;
        this.updatedDate = updatedDate;
        this.category = category;
    }

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

    public Timestamp getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(Timestamp updatedDate) {
        this.updatedDate = updatedDate;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Article article = (Article) o;
        return Objects.equals(id, article.id) &&
                Objects.equals(headline, article.headline) &&
                Objects.equals(content, article.content) &&
                Objects.equals(createdDate, article.createdDate) &&
                Objects.equals(updatedDate, article.updatedDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, headline, content, createdDate, updatedDate);
    }

    @Override
    public String toString() {
        return "Article{" +
                "headline='" + headline + '\'' +
                ", content='" + content + '\'' +
                ", createdDate=" + createdDate +
                ", updatedDate=" + updatedDate +
                '}';
    }
}
