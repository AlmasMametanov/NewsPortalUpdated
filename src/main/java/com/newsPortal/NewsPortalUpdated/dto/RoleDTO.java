package com.newsPortal.NewsPortalUpdated.dto;

import javax.validation.constraints.NotEmpty;

public class RoleDTO {
    private Long id;

    @NotEmpty(message = "RoleName не должен быть пустым")
    private String roleName;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }
}
