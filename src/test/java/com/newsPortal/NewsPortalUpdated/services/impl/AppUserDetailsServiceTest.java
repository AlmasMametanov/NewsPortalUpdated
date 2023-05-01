package com.newsPortal.NewsPortalUpdated.services.impl;

import com.newsPortal.NewsPortalUpdated.models.User;
import com.newsPortal.NewsPortalUpdated.repositories.UserRepository;
import com.newsPortal.NewsPortalUpdated.security.AppUserDetails;
import com.newsPortal.NewsPortalUpdated.util.UserNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AppUserDetailsServiceTest {
    @InjectMocks
    private AppUserDetailsService appUserDetailsService;

    @Mock
    private UserRepository userRepository;

    @Test
    void loadUserByUsernameWillThrowExceptionUserNotFound() {
        // given
        String username = "user@mail.com";
        when(userRepository.findByEmail(username)).thenReturn(Optional.empty());
        // when
        // then
        assertThatThrownBy(() -> appUserDetailsService.loadUserByUsername(username))
                .isInstanceOf(UserNotFoundException.class)
                .hasMessageContaining("User not found");
    }

    @Test
    void loadUserByUsernameWillReturnUserDetails() {
        // given
        String username = "user@mail.com";
        User user = new User(1L, username, "password");
        when(userRepository.findByEmail(username)).thenReturn(Optional.of(user));
        // when
        UserDetails userDetails = appUserDetailsService.loadUserByUsername(username);
        // then
        assertNotNull(userDetails);
        assertTrue(userDetails instanceof AppUserDetails);
        assertEquals(username, userDetails.getUsername());
    }
}
