package com.newsPortal.NewsPortalUpdated.controllers;

import com.newsPortal.NewsPortalUpdated.util.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class ExceptionHandlerController {

    private final Logger logger = LogManager.getLogger(this.getClass().getName());

    @ExceptionHandler(value = CategoryNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    private ErrorResponse handleCategoryNotFoundException(CategoryNotFoundException exception) {
        logger.info("Handle CategoryNotFoundException - " + exception);
        return new ErrorResponse(HttpStatus.NOT_FOUND.value(), exception.getMessage());
    }

    @ExceptionHandler(ArticleNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    private ErrorResponse handleArticleNotFoundException(ArticleNotFoundException exception) {
        logger.info("Handle ArticleNotFoundException - " + exception);
        return new ErrorResponse(HttpStatus.NOT_FOUND.value(), exception.getMessage());
    }

    @ExceptionHandler(EmailAlreadyExistsException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    private ErrorResponse handleEmailAlreadyExistsException(EmailAlreadyExistsException exception) {
        logger.info("Handle EmailAlreadyExistsException - " + exception);
        return new ErrorResponse(HttpStatus.CONFLICT.value(), exception.getMessage());
    }

    @ExceptionHandler(value = UserNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    private ErrorResponse handleUserNotFoundException(UserNotFoundException exception) {
        logger.info("Handle UserNotFoundException - " + exception);
        return new ErrorResponse(HttpStatus.NOT_FOUND.value(), exception.getMessage());
    }

    @ExceptionHandler(value = RoleNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    private ErrorResponse handleRoleNotFoundException(RoleNotFoundException exception) {
        logger.info("Handle RoleNotFoundException - " + exception);
        return new ErrorResponse(HttpStatus.NOT_FOUND.value(), exception.getMessage());
    }

    @ExceptionHandler(RoleAlreadyExistsException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    private ErrorResponse handleRoleAlreadyExistsException(RoleAlreadyExistsException exception) {
        logger.info("Handle RoleAlreadyExistsException - " + exception);
        return new ErrorResponse(HttpStatus.CONFLICT.value(), exception.getMessage());
    }
}
