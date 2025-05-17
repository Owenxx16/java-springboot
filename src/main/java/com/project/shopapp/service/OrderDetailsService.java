package com.project.shopapp.service;

import com.project.shopapp.dto.OrderDetailDTO;
import com.project.shopapp.exceptions.DataNotFoundException;
import com.project.shopapp.model.Order;
import com.project.shopapp.model.OrderDetails;
import com.project.shopapp.model.Products;
import com.project.shopapp.respository.OrderDetailsRespository;
import com.project.shopapp.respository.OrderRespository;
import com.project.shopapp.respository.ProductRespository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderDetailsService implements IOrderDetails{
    private final OrderDetailsRespository orderDetailsRespository;
    private final ProductRespository productRespository;
    private final OrderRespository orderRespository;
    private final ModelMapper modelMapper;
    @Override
    public OrderDetails createOrderDetails(OrderDetailDTO orderDetails) throws DataNotFoundException {
        Order oder = orderRespository.findById(orderDetails.getOrderId()).orElseThrow(() -> new DataNotFoundException("Nothing Found"));
        Products products = productRespository.findById(orderDetails.getProductId()).orElseThrow(() -> new DataNotFoundException("Products not found"));
        OrderDetails orderDetail = OrderDetails.builder()
                .order(oder)
                .product(products)
                .quantity(orderDetails.getQuantity())
                .price(orderDetails.getPrice())
                .totalMoney(orderDetails.getQuantity() * orderDetails.getPrice())
                .color(orderDetails.getColor())
                .build();
        return orderDetailsRespository.save(orderDetail);
    }

    @Override
    public OrderDetails getOrderDetails(long orderId) throws DataNotFoundException {
        OrderDetails orderDetails = orderDetailsRespository.findById(orderId).orElseThrow(() -> new DataNotFoundException("Nothing found"));
        return orderDetails;
    }

    @Override
    public OrderDetails updateOrderDetails(Long id, OrderDetailDTO orderDetails) throws DataNotFoundException {
        // check existing OrderDetails
        OrderDetails orderDetails1 = orderDetailsRespository.findById(id).orElseThrow(() -> new DataNotFoundException("Khong tim thay"));
        Order existingOrder = orderRespository.findById(orderDetails.getOrderId()).orElseThrow(() -> new DataNotFoundException("Khong thim thay don hang"));
        Products existingProducts = productRespository.findById(orderDetails.getProductId()).orElseThrow(() -> new DataNotFoundException("Khong tim thay san pham"));
        orderDetails1.setOrder(existingOrder);
        orderDetails1.setProduct(existingProducts);
        orderDetails1.setQuantity(orderDetails.getQuantity());
        orderDetails1.setPrice(orderDetails.getPrice());
        orderDetails1.setTotalMoney(orderDetails.getQuantity() * orderDetails.getPrice());
        orderDetails1.setColor(orderDetails.getColor());
        return orderDetailsRespository.save(orderDetails1);
    }

    @Override
    public void deleteOrderDetails(long orderId) {
        OrderDetails orderDetails = orderDetailsRespository.findById(orderId).orElse(null);
        if(orderDetails != null) {
            orderDetailsRespository.deleteById(orderId);
        }
    }

    @Override
    public List<OrderDetails> getAllOrderDetails() {
        return orderDetailsRespository.findAll();
    }

    @Override
    public List<OrderDetails> getOrderDetailsByOrderId(long orderId) {
        return orderDetailsRespository.findByOrderId(orderId);
    }
}
