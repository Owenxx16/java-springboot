package com.project.shopapp.service;

import com.project.shopapp.model.Role;

import java.util.List;

public interface IRoleService {
    Role createRole (Role role);
    Role getRoleById(Long id);
    List<Role> getAllRoles();
    Role updateRole(Long id,Role role);
    void deleteRole(Long id);
}
