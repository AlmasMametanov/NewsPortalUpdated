package com.newsPortal.NewsPortalUpdated.repositories;

import com.newsPortal.NewsPortalUpdated.models.Article;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ArticleRepository extends JpaRepository<Article, Long> {
    List<Article> findAllByCategoryId(Long categoryId);
}
