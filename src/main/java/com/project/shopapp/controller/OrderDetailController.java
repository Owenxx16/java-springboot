package com.project.shopapp.controller;

import com.project.shopapp.dto.OrderDetailDTO;
import com.project.shopapp.exceptions.DataNotFoundException;
import com.project.shopapp.model.OrderDetails;
import com.project.shopapp.responses.OrderDetailsResponse;
import com.project.shopapp.service.IOrderDetails;
import com.project.shopapp.service.OrderDetailsService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("${api.prefix}/oder_detail")
@RequiredArgsConstructor
public class OrderDetailController {
    private final IOrderDetails orderDetails;
    private final OrderDetailsService orderDetailsService;

    @PostMapping
    public ResponseEntity<?> createOrderDetails(@Valid @RequestBody OrderDetailDTO orderDetail, BindingResult bindingResult) {
        try {
        if (bindingResult.hasErrors()) {
            List<String> errorMessage = bindingResult.getFieldErrors()
                    .stream()
                    .map(fieldError -> fieldError.getDefaultMessage())
                    .toList();
            return ResponseEntity.badRequest().body(errorMessage);
        }
            OrderDetails orderDetails1 = orderDetails.createOrderDetails(orderDetail);
            return ResponseEntity.ok().body(OrderDetailsResponse.fromOrderDetails(orderDetails1)) ;
        } catch (DataNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getOrderDetail(@Valid @PathVariable("id") Long id) {
        try{
            OrderDetails orderDetails1 = orderDetails.getOrderDetails(id);
            return ResponseEntity.ok().body(OrderDetailsResponse.fromOrderDetails(orderDetails1)) ;
        }catch(Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("")
    public ResponseEntity<?> getAllOrderDetails() {
        List<OrderDetails> orderDetailsList = orderDetails.getAllOrderDetails();
        List<OrderDetailsResponse> orderDetailsResponses = orderDetailsList.stream().map(OrderDetailsResponse::fromOrderDetails).toList();
        return ResponseEntity.ok().body(orderDetailsResponses);
    }

    // lay ra danh sach cac order_details cua 1 order nao do
    @GetMapping("/order/{oderId}")
    public ResponseEntity<?> getOrderDetails(@PathVariable("oderId") Long oderId) {
        List<OrderDetails> orderDetailsList = orderDetails.getOrderDetailsByOrderId(oderId);
        List<OrderDetailsResponse> orderDetailsResponses = orderDetailsList.stream().map(OrderDetailsResponse::fromOrderDetails).toList();
        return ResponseEntity.ok().body(orderDetailsResponses);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateOrderDetails(@PathVariable("id") Long id,
                                                @RequestBody @Valid OrderDetailDTO orderDetail) throws DataNotFoundException {
        OrderDetails orderDetails1 = orderDetailsService.updateOrderDetails(id,orderDetail);
        return ResponseEntity.ok().body(OrderDetailsResponse.fromOrderDetails(orderDetails1)) ;
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteOrderDetails(@PathVariable("id") Long id) {
        orderDetails.deleteOrderDetails(id);
        return ResponseEntity.ok("deleted orderDetails with id " + id);
    }
}
