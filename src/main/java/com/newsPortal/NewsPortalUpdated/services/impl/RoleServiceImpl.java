package com.newsPortal.NewsPortalUpdated.services.impl;

import com.newsPortal.NewsPortalUpdated.models.Role;
import com.newsPortal.NewsPortalUpdated.repositories.RoleRepository;
import com.newsPortal.NewsPortalUpdated.services.RoleService;
import com.newsPortal.NewsPortalUpdated.util.RoleAlreadyExistsException;
import com.newsPortal.NewsPortalUpdated.util.RoleNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class RoleServiceImpl implements RoleService {
    private final RoleRepository roleRepository;

    @Autowired
    public RoleServiceImpl(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public Role findByRoleName(String roleName) {
        return roleRepository.findByRoleName(roleName).orElseThrow(() -> new RoleNotFoundException("Role not found"));
    }

    @Override
    @Transactional
    public void createRole(Role role) {
        if (roleExistence(role.getRoleName()))
            throw new RoleAlreadyExistsException("Role " + role.getRoleName() + " already exists");
        roleRepository.save(role);
    }

    @Override
    @Transactional
    public void updateRole(Role updatedRole) {
        Optional<Role> role = roleRepository.findById(updatedRole.getId());
        if (role.isEmpty()) {
            throw new RoleNotFoundException("Role not found");
        }
        role.get().setRoleName(updatedRole.getRoleName());
    }

    @Override
    @Transactional
    public void deleteById(Long roleId) {
        if (roleRepository.findById(roleId).isPresent())
            roleRepository.deleteById(roleId);
        else
            throw new RoleNotFoundException("Role not found");
    }

    @Override
    public Boolean roleExistence(String roleName) {
        return roleRepository.existsByRoleName(roleName);
    }
}
