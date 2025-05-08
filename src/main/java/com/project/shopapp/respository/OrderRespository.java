package com.project.shopapp.respository;

import com.project.shopapp.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRespository extends JpaRepository<Order, Long> {
    List<Order> findByUserId (Long userId);
}
