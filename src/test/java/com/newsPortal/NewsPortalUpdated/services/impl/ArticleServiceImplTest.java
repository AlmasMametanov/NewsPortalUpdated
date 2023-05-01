package com.newsPortal.NewsPortalUpdated.services.impl;

import com.newsPortal.NewsPortalUpdated.models.Article;
import com.newsPortal.NewsPortalUpdated.models.Category;
import com.newsPortal.NewsPortalUpdated.repositories.ArticleRepository;
import com.newsPortal.NewsPortalUpdated.util.ArticleNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ArticleServiceImplTest {

    @InjectMocks
    private ArticleServiceImpl articleService;

    @Mock
    private ArticleRepository articleRepository;

    @Test
    void canCreateArticle() {
        // given
        Article article = new Article();
        article.setHeadline("Article1");
        // when
        articleService.createArticle(article);
        // then
        ArgumentCaptor<Article> articleArgumentCaptor = ArgumentCaptor.forClass(Article.class);
        verify(articleRepository).save(articleArgumentCaptor.capture());

        Article savedArticle = articleArgumentCaptor.getValue();
        assertNotNull(savedArticle.getCreatedDate());
        assertEquals(article.getHeadline(), savedArticle.getHeadline());
    }

    @Test
    void willThrowExceptionWhenArticlesNotFoundByCategoryId() {
        // given
        Long categoryId = 1L;
        List<Article> articleList = new ArrayList<>();
        when(articleRepository.findAllByCategoryId(categoryId)).thenReturn(articleList);
        // when
        // then
        assertThatThrownBy(() -> articleService.findAllArticleByCategoryId(categoryId))
                .isInstanceOf(ArticleNotFoundException.class)
                .hasMessageContaining("Articles not found");
    }

    @Test
    void canFindArticlesByCategoryId() {
        // given
        Long categoryId = 1L;
        Article article1 = new Article();
        Article article2 = new Article();
        List<Article> articleList = List.of(article1, article2);
        when(articleRepository.findAllByCategoryId(categoryId)).thenReturn(articleList);
        // when
        List<Article> result = articleService.findAllArticleByCategoryId(categoryId);
        // then
        assertEquals(articleList.size(), result.size());
        assertTrue(result.contains(article1));
        assertTrue(result.contains(article2));
    }

    @Test
    void willThrowExceptionWhenArticleNotFoundByArticleId() {
        // given
        Long articleId = 1L;
        when(articleRepository.findById(articleId)).thenReturn(Optional.empty());
        // when
        // then
        assertThatThrownBy(() -> articleService.findById(articleId))
                .isInstanceOf(ArticleNotFoundException.class)
                .hasMessageContaining("Article not found");
    }

    @Test
    void canFindArticleByArticleId() {
        // given
        Long articleId = 1L;
        Article article = new Article();
        article.setHeadline("Article");
        when(articleRepository.findById(articleId)).thenReturn(Optional.of(article));
        // when
        Article foundArticle = articleService.findById(articleId);
        // then
        assertNotNull(foundArticle);
        assertEquals(article.getHeadline(), foundArticle.getHeadline());
    }

    @Test
    void willThrowExceptionArticleNotFoundWhenArticleUpdatesByArticleId() {
        // given
        Article updatedArticle = new Article();
        updatedArticle.setId(1L);
        when(articleRepository.findById(updatedArticle.getId())).thenReturn(Optional.empty());
        // when
        // then
        assertThatThrownBy(() -> articleService.updateById(updatedArticle))
                .isInstanceOf(ArticleNotFoundException.class)
                .hasMessageContaining("Article not found");
    }

    @Test
    void canUpdateArticleByArticleId() {
        // given
        Article updatedArticle = new Article(1L, "Update article", "Updated some content",
                Timestamp.valueOf(LocalDateTime.now()), new Category("Updated category"));
        Article existingArticle = new Article(1L, "Article", "Some content",
                Timestamp.valueOf(LocalDateTime.now()), new Category("Category"));
        when(articleRepository.findById(updatedArticle.getId())).thenReturn(Optional.of(existingArticle));
        // when
        articleService.updateById(updatedArticle);
        // then
        assertEquals(updatedArticle.getHeadline(), existingArticle.getHeadline());
        assertEquals(updatedArticle.getContent(), existingArticle.getContent());
        assertNotEquals(existingArticle.getUpdatedDate(), updatedArticle.getUpdatedDate());
        assertEquals(updatedArticle.getCategory(), existingArticle.getCategory());
        assertEquals(updatedArticle.getId(), existingArticle.getId());
    }

    @Test
    void willThrowExceptionArticleNotFoundWhenArticleDeletesByArticleId() {
        // given
        Long articleId = 1L;
        when(articleRepository.findById(articleId)).thenReturn(Optional.empty());
        // when
        // then
        assertThatThrownBy(() -> articleService.deleteById(articleId))
                .isInstanceOf(ArticleNotFoundException.class)
                .hasMessageContaining("Article not found");
    }

    @Test
    void canDeleteArticleByArticleId() {
        // given
        Long articleId = 1L;
        Article article = new Article();
        when(articleRepository.findById(articleId)).thenReturn(Optional.of(article));
        // when
        articleService.deleteById(articleId);
        // then
        verify(articleRepository).deleteById(articleId);
    }
}
