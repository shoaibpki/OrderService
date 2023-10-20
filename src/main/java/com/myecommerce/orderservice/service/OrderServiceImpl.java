package com.myecommerce.orderservice.service;

import com.myecommerce.orderservice.entity.Order;
import com.myecommerce.orderservice.entity.OrderDetails;
import com.myecommerce.orderservice.model.OrderItemList;
import com.myecommerce.orderservice.model.OrderRequest;
import com.myecommerce.orderservice.repository.OrderRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.stream.Collectors;

@Service
@Log4j2
public class OrderServiceImpl implements OrderService{

    @Autowired
    OrderRepository orderRepository;

    @Override
    public Long placeOrder(OrderRequest orderRequest) {
        log.info("Placing Order Request : {}", orderRequest);
        Order order = Order.builder()
                .orderDate(Instant.now())
                .status("Created")
                .totalAmount(orderRequest.getTotalAmount())
                .orderDetails(orderRequest.getOrderItemList().stream()
                        .map(this::getItems)
                        .collect(Collectors.toList()))
                .build();
        orderRepository.save(order);
        log.info("Order placed Successfully with order id : {}", order.getOrderId());
        return order.getOrderId();
    }

    private OrderDetails getItems(OrderItemList orderItemList) {
        log.info("save order item : {}", orderItemList);
        return OrderDetails.builder()
                .productId(orderItemList.getProductId())
                .quantity(orderItemList.getQuantity())
                .price(orderItemList.getPrice())
                .build();
    }


}
