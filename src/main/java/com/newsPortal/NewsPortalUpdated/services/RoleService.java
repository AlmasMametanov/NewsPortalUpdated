package com.newsPortal.NewsPortalUpdated.services;

import com.newsPortal.NewsPortalUpdated.models.Role;

public interface RoleService {
    void createRole(Role role);
    Role findByRoleName(String roleName);
    void updateRole(Role updatedRole);
    void deleteById(Long roleId);
    Boolean roleExistence(String roleName);
}
