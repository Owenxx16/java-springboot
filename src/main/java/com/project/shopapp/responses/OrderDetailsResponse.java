package com.project.shopapp.responses;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.project.shopapp.model.Order;
import com.project.shopapp.model.OrderDetails;
import com.project.shopapp.model.Products;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderDetailsResponse {
    private Long id;

    @JsonProperty("order_id")
    private Long orderId;
    @JsonProperty("product_id")
    private Long productId;

    private Float price;
    @JsonProperty( "number_of_product")
    private int quantity;

    @JsonProperty( "total_money")
    private Float totalMoney;

    private String color;

    public static OrderDetailsResponse fromOrderDetails(OrderDetails orderDetails) {
        OrderDetailsResponse orderDetailsResponse = OrderDetailsResponse.builder()
                .id(orderDetails.getId())
                .orderId(orderDetails.getOrder().getId())
                .productId(orderDetails.getProduct().getId())
                .price(orderDetails.getPrice())
                .quantity(orderDetails.getQuantity())
                .totalMoney(orderDetails.getTotalMoney())
                .color(orderDetails.getColor())
                .build();
        return orderDetailsResponse;
    }
}
