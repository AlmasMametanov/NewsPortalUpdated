package com.newsPortal.NewsPortalUpdated.services.impl;

import com.newsPortal.NewsPortalUpdated.repositories.UserRepository;
import com.newsPortal.NewsPortalUpdated.security.AppUserDetails;
import com.newsPortal.NewsPortalUpdated.util.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class AppUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Autowired
    public AppUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return new AppUserDetails(userRepository.findByEmail(username).orElseThrow(() -> new UserNotFoundException("User not found")));
    }
}
