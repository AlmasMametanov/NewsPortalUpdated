package com.newsPortal.NewsPortalUpdated.util;

public class PageNotFoundException extends RuntimeException {
    private String message;

    public PageNotFoundException() {
    }

    public PageNotFoundException(String message) {
        super(message);
        this.message = message;
    }
}
