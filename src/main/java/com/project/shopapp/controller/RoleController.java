package com.project.shopapp.controller;

import com.project.shopapp.model.Role;
import com.project.shopapp.service.IRoleService;
import com.project.shopapp.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("${api.prefix}/roles")
@RequiredArgsConstructor
public class RoleController {
    private final IRoleService roleService;

    @PostMapping("")
    public ResponseEntity<?> createRole(@RequestBody Role role) {
        roleService.createRole(role);
        return ResponseEntity.ok("created Role successfully");
    }

    @GetMapping("")
    public ResponseEntity<List<Role>> getAllRoles() {
        List<Role> roles = roleService.getAllRoles();
        return ResponseEntity.ok(roles);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Role> getRoleById(@PathVariable("id") Long id) {
        Role role1 = roleService.getRoleById(id);
        return ResponseEntity.ok(role1);
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateRole(@PathVariable("id") Long id, @RequestBody Role role) {
        roleService.updateRole(id, role);
        return ResponseEntity.ok("Updated Successfully");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteRole(@PathVariable("id") Long id) {
        roleService.deleteRole(id);
        return ResponseEntity.ok("Deleted Successfully");
    }
}
