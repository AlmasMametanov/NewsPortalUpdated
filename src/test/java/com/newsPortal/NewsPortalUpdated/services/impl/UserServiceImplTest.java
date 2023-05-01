package com.newsPortal.NewsPortalUpdated.services.impl;

import com.newsPortal.NewsPortalUpdated.models.Role;
import com.newsPortal.NewsPortalUpdated.models.User;
import com.newsPortal.NewsPortalUpdated.repositories.UserRepository;
import com.newsPortal.NewsPortalUpdated.util.EmailAlreadyExistsException;
import com.newsPortal.NewsPortalUpdated.util.UserNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {
    @InjectMocks
    private UserServiceImpl userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleServiceImpl roleService;

    @Test
    void willThrowExceptionUsersNotFoundWhenFindAllUser() {
        // given
        List<User> userList = new ArrayList<>();
        when(userRepository.findAll()).thenReturn(userList);
        // when
        // then
        assertThatThrownBy(() -> userService.findAll())
                .isInstanceOf(UserNotFoundException.class)
                .hasMessageContaining("Users not found!");
    }

    @Test
    void canFindAllUser() {
        // given
        User user1 = new User(1L, "user1@mail.com", "password1");
        User user2 = new User(2L, "user2@mail.com", "password2");
        List<User> userList = List.of(user1, user2);
        when(userRepository.findAll()).thenReturn(userList);
        // when
        List<User> result = userService.findAll();
        // then
        assertEquals(userList.size(), result.size());
        assertTrue(result.contains(user1));
        assertTrue(result.contains(user2));
    }

    @Test
    void willThrowExceptionUsersNotFoundWhenFindUserByEmail() {
        // given
        String email = "user@mail.com";
        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());
        // when
        // then
        assertThatThrownBy(() -> userService.findByEmail(email))
                .isInstanceOf(UserNotFoundException.class)
                .hasMessageContaining("User not found");
    }

    @Test
    void canFindUserByEmail() {
        // given
        String email = "user@mail.com";
        User user = new User(email, "password");
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        // when
        User result = userService.findByEmail(email);
        // then
        assertNotNull(result);
        assertEquals(email, result.getEmail());
    }

    @Test
    void userExistenceWillReturnTrueWhenUserExists() {
        // given
        String email = "user@mail.com";
        when(userRepository.existsByEmail(email)).thenReturn(true);
        // when
        Boolean userExistence = userService.userExistence(email);
        // then
        assertTrue(userExistence);
    }

    @Test
    void userExistenceWillReturnFalseWhenUserDoesNotExist() {
        // given
        String email = "user@mail.com";
        when(userRepository.existsByEmail(email)).thenReturn(false);
        // when
        Boolean userExistence = userService.userExistence(email);
        // then
        assertFalse(userExistence);
    }

    @Test
    void willThrowExceptionUserAlreadyExistsWhenCreateUser() {
        // given
        User user = new User("user@mail.com", "password");
        when(userRepository.existsByEmail(user.getEmail())).thenReturn(true);
        // when
        // then
        assertThatThrownBy(() -> userService.createUser(user))
                .isInstanceOf(EmailAlreadyExistsException.class)
                .hasMessageContaining("Email " + user.getEmail() + " taken");
        verify(userRepository, never()).save(user);
    }

    @Test
    void canCreateUser() {
        // given
        String email = "user@mail.com";
        User user = new User(email, "password");
        Role role = new Role(1L, "ROLE_USER");
        when(userRepository.existsByEmail(email)).thenReturn(false);
        when(roleService.findByRoleName("ROLE_USER")).thenReturn(role);
        // when
        userService.createUser(user);
        // then
        ArgumentCaptor<User> userArgumentCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(userArgumentCaptor.capture());

        User savedUser = userArgumentCaptor.getValue();
        assertEquals(email, savedUser.getEmail());
        assertEquals(1, savedUser.getRoleList().size());
    }

    @Test
    void willThrowExceptionUserNotFoundWhenUpdateEmailByUserId() {
        // given
        User updatedUser = new User(1L, "updatedUser@mail.com", "password");
        when(userRepository.findById(updatedUser.getId())).thenReturn(Optional.empty());
        // when
        // then
        assertThatThrownBy(() -> userService.updateEmailById(updatedUser))
                .isInstanceOf(UserNotFoundException.class)
                .hasMessageContaining("Email " + updatedUser.getEmail() + " not found");
    }

    @Test
    void willThrowExceptionEmailAlreadyExistsWhenUpdateEmailByUserId() {
        // given
        User updatedUser = new User(1L, "updatedUser@mail.com", "password");
        User existedUser = new User(1L, "user@mail.com", "password");
        when(userRepository.findById(updatedUser.getId())).thenReturn(Optional.of(existedUser));
        when(userRepository.existsByEmail(updatedUser.getEmail())).thenReturn(true);
        // when
        // then
        assertThatThrownBy(() -> userService.updateEmailById(updatedUser))
                .isInstanceOf(EmailAlreadyExistsException.class)
                .hasMessageContaining("Email " + updatedUser.getEmail() + " taken");
        verify(userRepository, never()).save(existedUser);
    }

    @Test
    void canUpdateEmailByUserId() {
        // given
        User updatedUser = new User(1L, "updatedUser@mail.com", "password");
        User existedUser = new User(1L, "user@mail.com", "password");
        when(userRepository.findById(updatedUser.getId())).thenReturn(Optional.of(existedUser));
        when(userRepository.existsByEmail(updatedUser.getEmail())).thenReturn(false);
        // when
        userService.updateEmailById(updatedUser);
        // then
        assertEquals(updatedUser.getEmail(), existedUser.getEmail());
        verify(userRepository).save(existedUser);
    }

    @Test
    void willThrowExceptionUserNotFoundWhenUpdatePasswordByUserId() {
        // given
        User updatedUser = new User(1L, "user@mail.com", "updatedPassword");
        when(userRepository.findById(updatedUser.getId())).thenReturn(Optional.empty());
        // when
        // then
        assertThatThrownBy(() -> userService.updatePasswordById(updatedUser))
                .isInstanceOf(UserNotFoundException.class)
                .hasMessageContaining("Email " + updatedUser.getEmail() + " not found");
    }

    @Test
    void canUpdatePasswordByUserId() {
        // given
        User updatedUser = new User(1L, "user@mail.com", "updatedPassword");
        User existedUser = new User(1L, "user@mail.com", "password");
        when(userRepository.findById(updatedUser.getId())).thenReturn(Optional.of(existedUser));
        // when
        userService.updatePasswordById(updatedUser);
        // then
        assertEquals(updatedUser.getPassword(), existedUser.getPassword());
        verify(userRepository).save(existedUser);
    }

    @Test
    void willThrowExceptionUserNotFoundWhenUpdateRoleByUserId() {
        // given
        User updatedUser = new User(1L, "user@mail.com");
        when(userRepository.findById(updatedUser.getId())).thenReturn(Optional.empty());
        // when
        // then
        assertThatThrownBy(() -> userService.updateRoleByUserId(updatedUser))
                .isInstanceOf(UserNotFoundException.class)
                .hasMessageContaining("Email " + updatedUser.getEmail() + " not found");
    }

    @Test
    void canUpdateRoleByUserId() {
        // given
        User updatedUser = new User(1L, "user@mail.com");
        Role role1 = new Role("ROLE_AUTHOR");
        Role role2 = new Role("ROLE_ADMIN");
        updatedUser.setRoleList(List.of(role1, role2));
        User existedUser = new User();
        when(userRepository.findById(updatedUser.getId())).thenReturn(Optional.of(existedUser));
        // when
        userService.updateRoleByUserId(updatedUser);
        // then
        verify(userRepository).save(existedUser);
        assertEquals(updatedUser.getRoleList().size(), existedUser.getRoleList().size());
        assertTrue(existedUser.getRoleList().contains(role1));
        assertTrue(existedUser.getRoleList().contains(role2));
    }
}
