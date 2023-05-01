package com.newsPortal.NewsPortalUpdated.controllers;

import com.newsPortal.NewsPortalUpdated.dto.ArticleDTO;
import com.newsPortal.NewsPortalUpdated.models.Article;
import com.newsPortal.NewsPortalUpdated.services.ArticleService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class ArticleController {

    private final ArticleService articleService;
    private final ModelMapper modelMapper;
    private final Logger logger = LogManager.getLogger(this.getClass().getName());

    @Autowired
    public ArticleController(ArticleService articleService, ModelMapper modelMapper) {
        this.articleService = articleService;
        this.modelMapper = modelMapper;
    }

    @GetMapping("/{categoryId}/articles")
    public ResponseEntity<List<ArticleDTO>> getAllArticleByCategoryId(@PathVariable("categoryId") Long categoryId) {
        return ResponseEntity.ok(articleService.findAllArticleByCategoryId(categoryId).stream().map(this::mapToArticleDTO)
                .collect(Collectors.toList()));
    }

    @GetMapping("/articles/{articleId}")
    public ResponseEntity<ArticleDTO> getArticleById(@PathVariable("articleId") Long articleId) {
        return ResponseEntity.ok(mapToArticleDTO(articleService.findById(articleId)));
    }

    @PostMapping("/article")
    public ResponseEntity<Object> createArticle(@RequestBody @Valid ArticleDTO articleDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            logger.info("Have errors in request - " + bindingResult.getAllErrors());
            return ResponseEntity.badRequest().body(bindingResult.getAllErrors());
        }
        articleService.createArticle(mapToArticle(articleDTO));
        return ResponseEntity.ok().build();
    }

    @PutMapping("/article")
    public ResponseEntity<Object> updateArticle(@RequestBody @Valid ArticleDTO articleDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            logger.info("Have errors in request - " + bindingResult.getAllErrors());
            return ResponseEntity.badRequest().body(bindingResult.getAllErrors());
        }
        articleService.updateById(mapToArticle(articleDTO));
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/articles/{articleId}")
    public ResponseEntity<Object> deleteArticle(@PathVariable("articleId") Long articleId) {
        articleService.deleteById(articleId);
        return ResponseEntity.ok().build();
    }

    private Article mapToArticle(ArticleDTO articleDTO) {
        return modelMapper.map(articleDTO, Article.class);
    }

    private ArticleDTO mapToArticleDTO(Article article) {
        return modelMapper.map(article, ArticleDTO.class);
    }
}
