package com.myecommerce.orderservice.service;

import com.myecommerce.orderservice.model.OrderRequest;
import com.myecommerce.orderservice.model.OrderResponse;

public interface OrderService {
    Long placeOrder(OrderRequest orderRequest);

    OrderResponse getOrderById(long orderId);
}
