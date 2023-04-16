package com.newsPortal.NewsPortalUpdated.services.impl;

import com.newsPortal.NewsPortalUpdated.models.Article;
import com.newsPortal.NewsPortalUpdated.repositories.ArticleRepository;
import com.newsPortal.NewsPortalUpdated.services.ArticleService;
import com.newsPortal.NewsPortalUpdated.util.ArticleNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class ArticleServiceImpl implements ArticleService {
    private final ArticleRepository articleRepository;

    @Autowired
    public ArticleServiceImpl(ArticleRepository articleRepository) {
        this.articleRepository = articleRepository;
    }

    @Override
    @Transactional
    public void createArticle(Article article) {
        article.setCreatedDate(Timestamp.valueOf(LocalDateTime.now()));
        articleRepository.save(article);
    }

    @Override
    public List<Article> findAllArticleByCategoryId(Long categoryId) {
        List<Article> articleList = articleRepository.findAllByCategoryId(categoryId);
        if (!articleList.isEmpty())
            return articleList;
        throw new ArticleNotFoundException("Articles not found");
    }

    @Override
    public Article findById(Long articleId) {
        return articleRepository.findById(articleId).orElseThrow(() -> new ArticleNotFoundException("Article not found"));
    }

    @Override
    @Transactional
    public void updateById(Article updatedArticle) {
        Optional<Article> article = articleRepository.findById(updatedArticle.getId());
        if (article.isPresent()) {
            article.get().setHeadline(updatedArticle.getHeadline());
            article.get().setContent(updatedArticle.getContent());
            article.get().setUpdatedDate(Timestamp.valueOf(LocalDateTime.now()));
            article.get().setCategory(updatedArticle.getCategory());
        } else {
            throw new ArticleNotFoundException("Article not found");
        }
    }

    @Override
    @Transactional
    public void deleteById(Long articleId) {
        Optional<Article> article = articleRepository.findById(articleId);
        if (article.isPresent())
            articleRepository.deleteById(articleId);
        else
            throw new ArticleNotFoundException("Article not found");
    }
}
