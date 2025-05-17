package com.project.shopapp.service;

import com.project.shopapp.dto.OrderDetailDTO;
import com.project.shopapp.exceptions.DataNotFoundException;
import com.project.shopapp.model.OrderDetails;

import java.util.List;

public interface IOrderDetails {
    OrderDetails createOrderDetails(OrderDetailDTO orderDetails) throws DataNotFoundException;
    OrderDetails getOrderDetails(long orderId) throws DataNotFoundException;
    OrderDetails updateOrderDetails(Long id, OrderDetailDTO orderDetails) throws DataNotFoundException;
    void deleteOrderDetails(long orderId);
    List<OrderDetails> getAllOrderDetails();
    List<OrderDetails> getOrderDetailsByOrderId(long orderId);

}
