package com.newsPortal.NewsPortalUpdated.controllers;

import com.newsPortal.NewsPortalUpdated.dto.CategoryDTO;
import com.newsPortal.NewsPortalUpdated.models.Category;
import com.newsPortal.NewsPortalUpdated.services.CategoryService;
import com.newsPortal.NewsPortalUpdated.util.CategoryNotFoundException;
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
public class CategoryController {
    private final ModelMapper modelMapper;
    private final CategoryService categoryService;

    @Autowired
    public CategoryController(ModelMapper modelMapper, CategoryService categoryService) {
        this.modelMapper = modelMapper;
        this.categoryService = categoryService;
    }

    @GetMapping("/categories") // здесь redis использовать нужно, чтобы кэшировать категории
    public ResponseEntity<List<CategoryDTO>> getAllCategory() {
        return ResponseEntity.ok(categoryService.findAllCategory(null).stream().map(this::mapToCategoryDTO)
                .collect(Collectors.toList()));
    }

    @PostMapping("/category")
    public ResponseEntity<Object> createCategory(@RequestBody @Valid CategoryDTO categoryDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            return ResponseEntity.badRequest().body(bindingResult.getAllErrors());
        categoryService.createCategory(mapToCategory(categoryDTO));
        return ResponseEntity.ok().build();
    }

    @PutMapping("/category")
    public ResponseEntity<Object> updateCategory(@RequestBody @Valid CategoryDTO categoryDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            return ResponseEntity.badRequest().body(bindingResult.getAllErrors());
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

    @ExceptionHandler(value = CategoryNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    private ErrorResponse handleCategoryNotFoundException(CategoryNotFoundException exception) {
        return new ErrorResponse(HttpStatus.NOT_FOUND.value(), exception.getMessage());
    }

}
