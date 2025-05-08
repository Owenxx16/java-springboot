package com.project.shopapp.service;

import com.project.shopapp.model.Role;
import com.project.shopapp.respository.RoleRespository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RoleService implements IRoleService {
    private final RoleRespository roleRespository;

    @Override
    public Role createRole(Role role) {
        return roleRespository.save(role);
    }

    @Override
    public Role getRoleById(Long id) {
        return roleRespository.findById(id).orElseThrow(() -> new RuntimeException("Role not found"));
    }

    @Override
    public List<Role> getAllRoles() {
        return roleRespository.findAll();
    }

    @Override
    public Role updateRole(Long id, Role role) {
        Role oldRole = getRoleById(id);
        oldRole.setName(role.getName());
        return roleRespository.save(oldRole);
    }

    @Override
    public void deleteRole(Long id) {
        roleRespository.deleteById(id);
    }
}
