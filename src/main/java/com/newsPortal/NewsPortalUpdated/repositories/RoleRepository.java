package com.newsPortal.NewsPortalUpdated.repositories;

import com.newsPortal.NewsPortalUpdated.models.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByRoleName(String roleName);
    Boolean existsByRoleName(String roleName);
}
