package com.myecommerce.orderservice.service;

import com.myecommerce.orderservice.model.OrderRequest;

public interface OrderService {
    Long placeOrder(OrderRequest orderRequest);
}
