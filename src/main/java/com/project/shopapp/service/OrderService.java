package com.project.shopapp.service;

import com.project.shopapp.dto.OrderDTO;
import com.project.shopapp.exceptions.DataNotFoundException;
import com.project.shopapp.model.Order;
import com.project.shopapp.model.OrderStatus;
import com.project.shopapp.model.User;
import com.project.shopapp.responses.OrderResponse;
import com.project.shopapp.respository.OrderRespository;
import com.project.shopapp.respository.UserRespository;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService implements IOrderService {
    private final OrderRespository orderRespository;
    private final UserRespository userRespository;
    private final ModelMapper modelMapper;

    @Override
    public Order createOrder(OrderDTO orderDTO) throws DataNotFoundException {
        // tìm User có tồn tại hay không
       User user = userRespository.findById(orderDTO.getUserId()).orElseThrow(() -> new DataNotFoundException("User not found" + orderDTO.getUserId()));
//        // convert orderDTO => order
//        // dùng thư viện modelMapper
//        // Tạo 1 luồng bảng ánh xạ riêng để kiểm soát việc ánh xạ giữa 2 bảng (bỏ đi id vì trong bảng Order không có id)
        modelMapper.typeMap(OrderDTO.class, Order.class)
                .addMappings(mapper -> mapper.skip(Order::setId));
//        // Cập nhật các trường của đơn hàng từ oderDTO
       Order order = new Order();
//        // ném các properties của oderDTO sang cho order
        modelMapper.map(orderDTO, order);
       order.setUser(user);
        order.setOrderDate(new Date());
        order.setStatus(OrderStatus.PENDING);
//        Date shippedDate = orderDTO.getShippingDate() == null ? new Date() : orderDTO.getShippingDate();
       LocalDate shippedDate = orderDTO.getShippingDate() == null ? LocalDate.now() : orderDTO.getShippingDate();
       if(shippedDate.isBefore(LocalDate.now())) {
           throw new DataNotFoundException("Shipping Date không thể giao trước ngày hôm nay");
        }
        order.setShippingDate(shippedDate);
       order.setActive(true);
        orderRespository.save(order);
//        // ánh xạ từ order sang cho OderResponse
//        modelMapper.typeMap(Order.class, OrderResponse.class);
//        OrderResponse orderResponse = new OrderResponse();
//        modelMapper.map(order, orderResponse);
        return order;
    }

    @Override
    public Order getOrderById(Long id) {
        return null;
    }

    @Override
    public Order updateOrder(Long id, OrderDTO orderDTO) {
        return null;
    }

    @Override
    public List<Order> getAllOrders() {
        return List.of();
    }
//    @Override
//    public OrderResponse createOrder(OrderDTO orderDTO) throws DataNotFoundException {
//        // tìm User có tồn tại hay không
//        User user = userRespository.findById(orderDTO.getUserId()).orElseThrow(() -> new DataNotFoundException("User not found" + orderDTO.getUserId()));
//        // convert orderDTO => order
//        // dùng thư viện modelMapper
//        // Tạo 1 luồng bảng ánh xạ riêng để kiểm soát việc ánh xạ giữa 2 bảng (bỏ đi id vì trong bảng Order không có id)
//        modelMapper.typeMap(OrderDTO.class, Order.class)
//                .addMappings(mapper -> mapper.skip(Order::setId));
//        // Cập nhật các trường của đơn hàng từ oderDTO
//        Order order = new Order();
//        // ném các properties của oderDTO sang cho order
//        modelMapper.map(orderDTO, order);
//        order.setUser(user);
//        order.setOrderDate(new Date());
//        order.setStatus(OrderStatus.PENDING);
////        Date shippedDate = orderDTO.getShippingDate() == null ? new Date() : orderDTO.getShippingDate();
//        LocalDate shippedDate = orderDTO.getShippingDate() == null ? LocalDate.now() : orderDTO.getShippingDate();
//        if(shippedDate.isBefore(LocalDate.now())) {
//            throw new DataNotFoundException("Shipping Date không thể giao trước ngày hôm nay");
//        }
//        order.setShippingDate(shippedDate);
//        order.setActive(true);
//        orderRespository.save(order);
//        // ánh xạ từ order sang cho OderResponse
//        modelMapper.typeMap(Order.class, OrderResponse.class);
//        OrderResponse orderResponse = new OrderResponse();
//        modelMapper.map(order, orderResponse);
//        return orderResponse;
//    }
//
//    @Override
//    public OrderResponse getOrderById(Long id) {
//        return null;
//    }
//
//    @Override
//    public OrderResponse updateOrder(Long id, OrderDTO orderDTO) {
//        return null;
//    }
//
//    @Override
//    public List<OrderResponse> getAllOrders() {
//        return List.of();
//    }
}
