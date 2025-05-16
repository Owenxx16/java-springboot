package com.project.shopapp.service;

import com.project.shopapp.dto.OrderDTO;
import com.project.shopapp.exceptions.DataNotFoundException;
import com.project.shopapp.model.Order;
import com.project.shopapp.responses.OrderResponse;

import java.util.List;

public interface IOrderService {
//    OrderResponse createOrder(OrderDTO orderDTO) throws DataNotFoundException;
//    OrderResponse getOrderById(Long id);
//    OrderResponse updateOrder(Long id,OrderDTO orderDTO);
//    List<OrderResponse> getAllOrders();

      Order createOrder(OrderDTO orderDTO) throws DataNotFoundException;
      Order getOrderById(Long id);
      Order updateOrder(Long id,OrderDTO orderDTO) throws DataNotFoundException;
      List<Order> getAllOrders();
      List<Order> getOrdersByUserId(Long userId);
      void deleteOrder(Long id);
}
