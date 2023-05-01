package com.newsPortal.NewsPortalUpdated.controllers;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.newsPortal.NewsPortalUpdated.dto.CategoryDTO;
import com.newsPortal.NewsPortalUpdated.models.Category;
import com.newsPortal.NewsPortalUpdated.services.CategoryService;
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
public class CategoryController {
    private final ModelMapper modelMapper;
    private final CategoryService categoryService;
    private final Logger logger = LogManager.getLogger(this.getClass().getName());

    @Autowired
    public CategoryController(ModelMapper modelMapper, CategoryService categoryService) {
        this.modelMapper = modelMapper;
        this.categoryService = categoryService;
    }

    @GetMapping("/categories")
    public ResponseEntity<List<CategoryDTO>> getAllCategory() {
        ObjectMapper mapper = new ObjectMapper();
        return ResponseEntity.ok(mapper.convertValue(categoryService.findAllCategory(null), new TypeReference<List<Category>>(){}).stream().map(this::mapToCategoryDTO)
                .collect(Collectors.toList()));
    }

    @PostMapping("/category")
    public ResponseEntity<Object> createCategory(@RequestBody @Valid CategoryDTO categoryDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            logger.info("Have errors in request - " + bindingResult.getAllErrors());
            return ResponseEntity.badRequest().body(bindingResult.getAllErrors());
        }
        categoryService.createCategory(mapToCategory(categoryDTO));
        return ResponseEntity.ok().build();
    }

    @PutMapping("/category")
    public ResponseEntity<Object> updateCategory(@RequestBody @Valid CategoryDTO categoryDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            logger.info("Have errors in request - " + bindingResult.getAllErrors());
            return ResponseEntity.badRequest().body(bindingResult.getAllErrors());
        }
        categoryService.updateCategory(mapToCategory(categoryDTO));
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/categories/{categoryId}")
    public ResponseEntity<Object> deleteCategory(@PathVariable("categoryId") Long categoryId) {
        categoryService.deleteById(categoryId);
        return ResponseEntity.ok().build();
    }

    private CategoryDTO mapToCategoryDTO(Category category) {
        return modelMapper.map(category, CategoryDTO.class);
    }

    private Category mapToCategory(CategoryDTO categoryDTO) {
        return modelMapper.map(categoryDTO, Category.class);
    }

}
