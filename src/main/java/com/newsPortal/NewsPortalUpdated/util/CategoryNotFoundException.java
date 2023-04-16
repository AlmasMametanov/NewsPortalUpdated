package com.newsPortal.NewsPortalUpdated.util;

public class CategoryNotFoundException extends RuntimeException {
    private String message;

    public CategoryNotFoundException() {
    }

    public CategoryNotFoundException(String message) {
        super(message);
        this.message = message;
    }
}
