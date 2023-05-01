package com.newsPortal.NewsPortalUpdated.services.impl;

import com.newsPortal.NewsPortalUpdated.models.Role;
import com.newsPortal.NewsPortalUpdated.repositories.RoleRepository;
import com.newsPortal.NewsPortalUpdated.util.RoleAlreadyExistsException;
import com.newsPortal.NewsPortalUpdated.util.RoleNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class RoleServiceImplTest {
    @InjectMocks
    private RoleServiceImpl roleService;

    @Mock
    private RoleRepository roleRepository;

    @Test
    void willThrowExceptionRoleNotFoundWhenFindRoleByRoleName() {
        // given
        String roleName = "ROLE_USER";
        when(roleRepository.findByRoleName(roleName)).thenReturn(Optional.empty());
        // when
        // then
        assertThatThrownBy(() -> roleService.findByRoleName(roleName))
                .isInstanceOf(RoleNotFoundException.class)
                .hasMessageContaining("Role not found");
    }

    @Test
    void canFindRoleByRoleName() {
        // given
        String roleName = "ROLE_USER";
        Role role = new Role(1L, roleName);
        when(roleRepository.findByRoleName(roleName)).thenReturn(Optional.of(role));
        // when
        Role result = roleService.findByRoleName(roleName);
        // then
        assertNotNull(result);
        assertEquals(roleName, result.getRoleName());
    }

    @Test
    void willThrowExceptionRoleAlreadyExistsWhenCreateRole() {
        // given
        Role role = new Role("ROLE_USER");
        when(roleRepository.existsByRoleName(role.getRoleName())).thenReturn(true);
        // when
        // then
        assertThatThrownBy(() -> roleService.createRole(role))
                .isInstanceOf(RoleAlreadyExistsException.class)
                .hasMessageContaining("Role " + role.getRoleName() + " already exists");
        verify(roleRepository, never()).save(role);
    }

    @Test
    void canCreateRole() {
        // given
        Role role = new Role("ROLE_USER");
        when(roleRepository.existsByRoleName(role.getRoleName())).thenReturn(false);
        // when
        roleService.createRole(role);
        // then
        ArgumentCaptor<Role> roleArgumentCaptor = ArgumentCaptor.forClass(Role.class);
        verify(roleRepository).save(roleArgumentCaptor.capture());

        Role savedRole = roleArgumentCaptor.getValue();
        assertEquals(role.getRoleName(), savedRole.getRoleName());
    }

    @Test
    void willThrowExceptionRoleNotFoundWhenUpdateRole() {
        // given
        Role updatedRole = new Role(1L, "ROLE_AUTHOR");
        when(roleRepository.findById(updatedRole.getId())).thenReturn(Optional.empty());
        // when
        // then
        assertThatThrownBy(() -> roleService.updateRole(updatedRole))
                .isInstanceOf(RoleNotFoundException.class)
                .hasMessageContaining("Role not found");
        verify(roleRepository, never()).save(any());
    }

    @Test
    void canUpdateRole() {
        // given
        Role updatedRole = new Role(1L, "ROLE_AUTHOR");
        Role existedRole = new Role(1L, "ROLE_USER");
        when(roleRepository.findById(updatedRole.getId())).thenReturn(Optional.of(existedRole));
        // when
        roleService.updateRole(updatedRole);
        // then
        assertEquals(updatedRole.getRoleName(), existedRole.getRoleName());
        assertEquals(updatedRole.getId(), existedRole.getId());
    }

    @Test
    void willThrowExceptionRoleNotFoundWhenDeleteRoleById() {
        // given
        Long roleId = 1L;
        when(roleRepository.findById(roleId)).thenReturn(Optional.empty());
        // when
        // then
        assertThatThrownBy(() -> roleService.deleteById(roleId))
                .isInstanceOf(RoleNotFoundException.class)
                .hasMessageContaining("Role not found");
    }

    @Test
    void canDeleteRole() {
        // given
        Long roleId = 1L;
        Role role = new Role(roleId, "ROLE_USER");
        when(roleRepository.findById(roleId)).thenReturn(Optional.of(role));
        // when
        roleService.deleteById(roleId);
        // then
        verify(roleRepository).deleteById(roleId);
    }

    @Test
    void roleExistenceShouldReturnTrueWhenRoleExists() {
        // given
        String roleName = "ROLE_USER";
        when(roleRepository.existsByRoleName(roleName)).thenReturn(true);
        // when
        Boolean result = roleService.roleExistence(roleName);
        // then
        assertTrue(result);
    }

    @Test
    void roleExistenceShouldReturnFalseWhenRoleDoesNotExist() {
        // given
        String roleName = "ROLE_USER";
        when(roleRepository.existsByRoleName(roleName)).thenReturn(false);
        // when
        Boolean result = roleService.roleExistence(roleName);
        // then
        assertFalse(result);
    }
}
