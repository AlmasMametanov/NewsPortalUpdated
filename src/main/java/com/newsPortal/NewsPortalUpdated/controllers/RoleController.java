package com.newsPortal.NewsPortalUpdated.controllers;

import com.newsPortal.NewsPortalUpdated.dto.RoleDTO;
import com.newsPortal.NewsPortalUpdated.models.Role;
import com.newsPortal.NewsPortalUpdated.services.RoleService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/rolesManagement")
public class RoleController {
    private final ModelMapper modelMapper;
    private final RoleService roleService;
    private final Logger logger = LogManager.getLogger(this.getClass().getName());

    @Autowired
    public RoleController(ModelMapper modelMapper, RoleService roleService) {
        this.modelMapper = modelMapper;
        this.roleService = roleService;
    }

    @GetMapping("/role/{roleName}")
    public ResponseEntity<RoleDTO> getRoleByRoleName(@PathVariable("roleName") String roleName) {
        return ResponseEntity.ok().body(mapToRoleDTO(roleService.findByRoleName(roleName)));
    }

    @PostMapping("/role")
    public ResponseEntity<Object> createRole(@RequestBody @Valid RoleDTO roleDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            logger.info("Have errors in request - " + bindingResult.getAllErrors());
            return ResponseEntity.badRequest().body(bindingResult.getAllErrors());
        }
        roleService.createRole(mapToRole(roleDTO));
        return ResponseEntity.ok().build();
    }

    @PutMapping("/role")
    public ResponseEntity<Object> updateRole(@RequestBody @Valid RoleDTO roleDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            logger.info("Have errors in request - " + bindingResult.getAllErrors());
            return ResponseEntity.badRequest().body(bindingResult.getAllErrors());
        }
        roleService.updateRole(mapToRole(roleDTO));
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/roles/{roleId}")
    public ResponseEntity<Object> deleteRole(@PathVariable("roleId") Long roleId) {
        roleService.deleteById(roleId);
        return ResponseEntity.ok().build();
    }

    private Role mapToRole(RoleDTO roleDTO) {
        return modelMapper.map(roleDTO, Role.class);
    }

    private RoleDTO mapToRoleDTO(Role role) {
        return modelMapper.map(role, RoleDTO.class);
    }
}
