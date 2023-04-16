package com.newsPortal.NewsPortalUpdated.services.impl;

import com.newsPortal.NewsPortalUpdated.models.User;
import com.newsPortal.NewsPortalUpdated.repositories.UserRepository;
import com.newsPortal.NewsPortalUpdated.services.UserService;
import com.newsPortal.NewsPortalUpdated.util.EmailAlreadyExistsException;
import com.newsPortal.NewsPortalUpdated.util.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public List<User> findAll() {
        List<User> userList = userRepository.findAll();
        if (!userList.isEmpty())
            return userList;
        else
            throw new UserNotFoundException("Users not found!");
    }

    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(() -> new UserNotFoundException("User not found"));
    }

    @Override
    public Boolean userExistence(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    @Transactional
    public void createUser(User user) {
        if (userExistence(user.getEmail()))
            throw new EmailAlreadyExistsException("Email " + user.getEmail() + " taken");
        userRepository.save(user);
    }

    @Override
    @Transactional
    public void updateEmailById(User updatedUser) {
        Optional<User> user = userRepository.findById(updatedUser.getId());
        if (user.isPresent()) {
            if (userExistence(updatedUser.getEmail()))
                throw new EmailAlreadyExistsException("Email " + user.get().getEmail() + " taken");
            user.get().setEmail(updatedUser.getEmail());
            userRepository.save(user.get());
        } else {
            throw new UserNotFoundException("Email " + updatedUser.getEmail() + " not found");
        }
    }

    @Override
    @Transactional
    public void updatePasswordById(User updatedUser) {
        Optional<User> user = userRepository.findById(updatedUser.getId());
        if (user.isPresent()) {
            user.get().setPassword(updatedUser.getPassword());
            userRepository.save(user.get());
        } else {
            throw new UserNotFoundException("Email " + updatedUser.getEmail() + " not found");
        }
    }
}
