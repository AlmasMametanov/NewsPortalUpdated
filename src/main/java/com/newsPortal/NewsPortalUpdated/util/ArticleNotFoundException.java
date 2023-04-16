package com.newsPortal.NewsPortalUpdated.util;

public class ArticleNotFoundException extends RuntimeException {
    private String message;

    public ArticleNotFoundException() {
    }

    public ArticleNotFoundException(String message) {
        super(message);
        this.message = message;
    }
}
