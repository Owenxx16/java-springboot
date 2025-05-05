package com.project.shopapp.controller;

import com.project.shopapp.dto.OrderDTO;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("${api.prefix}/orders")
public class OrderController {

    @PostMapping("")
    public ResponseEntity<?> createOrder (@RequestBody @Valid OrderDTO orderDTO,
                                          BindingResult bindingResult) {
        try{
            if (bindingResult.hasErrors()) {
                List<String> errorMessage = bindingResult.getFieldErrors()
                        .stream()
                        .map(fieldError -> fieldError.getDefaultMessage())
                        .toList();
                return ResponseEntity.badRequest().body(errorMessage);
            }
            return ResponseEntity.ok("createOrder successfully");
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/{userId}")
    public ResponseEntity<?> getOrderById(@PathVariable("userId") Long userId) {
        try{
            return ResponseEntity.ok("getOrderById successfully");
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    // Admin's job
    @PutMapping("/{id}")
    public ResponseEntity<?> updateOrder(@Valid @PathVariable ("id") Long id,@Valid @RequestBody OrderDTO orderDTO) {
        try{
            return ResponseEntity.ok("updateOrder successfully");
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteOrder(@PathVariable ("id") Long id) {
        // Xoa mem ==> chuyen active thanh false
        return ResponseEntity.ok("deleteOrder successfully");
    }
}
