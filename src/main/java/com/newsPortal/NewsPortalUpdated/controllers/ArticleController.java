package com.newsPortal.NewsPortalUpdated.controllers;

import com.newsPortal.NewsPortalUpdated.dto.ArticleDTO;
import com.newsPortal.NewsPortalUpdated.models.Article;
import com.newsPortal.NewsPortalUpdated.services.ArticleService;
import com.newsPortal.NewsPortalUpdated.util.ArticleNotFoundException;
import com.newsPortal.NewsPortalUpdated.util.ErrorResponse;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
        if (bindingResult.hasErrors())
            return ResponseEntity.badRequest().body(bindingResult.getAllErrors());
        articleService.createArticle(mapToArticle(articleDTO));
        return ResponseEntity.ok().build();
    }

    @PutMapping("/article")
    public ResponseEntity<Object> updateArticle(@RequestBody @Valid ArticleDTO articleDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            return ResponseEntity.badRequest().body(bindingResult.getAllErrors());
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

    @ExceptionHandler(ArticleNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    private ErrorResponse handleArticleNotFoundException(ArticleNotFoundException exception) {
        return new ErrorResponse(HttpStatus.NOT_FOUND.value(), exception.getMessage());
    }
}
