package com.newsPortal.NewsPortalUpdated.util;

public class UserNotFoundException extends RuntimeException {
    private String message;

    public UserNotFoundException() {
    }

    public UserNotFoundException(String message) {
        super(message);
        this.message = message;
    }
}
