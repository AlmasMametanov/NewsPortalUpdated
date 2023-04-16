package com.newsPortal.NewsPortalUpdated.util;

import com.newsPortal.NewsPortalUpdated.models.User;
import com.newsPortal.NewsPortalUpdated.services.UserService;
import com.newsPortal.NewsPortalUpdated.services.impl.AppUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class UserValidator implements Validator {
    private final UserService userService;

    @Autowired
    public UserValidator(UserService userService) {
        this.userService = userService;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return User.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        User user = (User)target;

        if (userService.userExistence(user.getEmail()))
            errors.rejectValue("email", "", "Email already exists");
    }
}
