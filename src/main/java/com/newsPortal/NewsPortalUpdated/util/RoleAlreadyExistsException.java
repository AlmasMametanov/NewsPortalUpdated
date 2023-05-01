package com.newsPortal.NewsPortalUpdated.util;

public class RoleAlreadyExistsException extends RuntimeException {
    private String message;

    public RoleAlreadyExistsException() {
    }

    public RoleAlreadyExistsException(String message) {
        super(message);
        this.message = message;
    }
}
