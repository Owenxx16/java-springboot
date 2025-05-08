package com.project.shopapp.respository;

import com.project.shopapp.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRespository extends JpaRepository<Role, Long> {
}
