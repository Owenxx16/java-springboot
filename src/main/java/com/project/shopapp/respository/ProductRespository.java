package com.project.shopapp.respository;

import com.project.shopapp.model.Products;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Pageable;

public interface ProductRespository extends JpaRepository<Products, Long> {
    boolean existsByName(String name);

    Page<Products> findAll (Pageable pageable); // phan trang san pham
}
