package com.project.shopapp.controller;

import com.project.shopapp.dto.OrderDetailDTO;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("${api.prefix}/oder_detail")
public class OrderDetailController {
    @PostMapping
    public ResponseEntity<?> createOrderDetails(OrderDetailDTO orderDetail) {
        return ResponseEntity.ok().body("hahaha");
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getOrderDetail(@PathVariable("id") int id) {
        try{
            return ResponseEntity.ok("AHAHAHA");
        }catch(Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // lay ra danh sach cac order_details cua 1 order nao do
    @GetMapping("/order/{oderId}")
    public ResponseEntity<?> getOrderDetails(@PathVariable("oderId") Long oderId) {
        return ResponseEntity.ok("AHAHAHA");
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateOrderDetails(@PathVariable("id") int id,
                                                @RequestBody @Valid OrderDetailDTO orderDetail) {
        return ResponseEntity.ok("updateOrderDetails with id " + id + ",orderDetail " + orderDetail);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteOrderDetails(@PathVariable("id") int id) {
        return ResponseEntity.ok("deleteOrderDetails with id " + id);
    }
}
