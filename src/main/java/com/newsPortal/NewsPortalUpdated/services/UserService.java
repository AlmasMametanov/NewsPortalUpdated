package com.newsPortal.NewsPortalUpdated.services;

import com.newsPortal.NewsPortalUpdated.models.User;

import java.util.List;

public interface UserService {
    List<User> findAll();
    User findByEmail(String email);
    Boolean userExistence(String email);
    void createUser(User user);
    void updateEmailById(User updatedUser);
    void updatePasswordById(User updatedUser);
    void updateRoleByUserId(User updatedUser);
}
