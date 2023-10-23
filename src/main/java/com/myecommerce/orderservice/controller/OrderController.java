package com.myecommerce.orderservice.controller;

import com.myecommerce.orderservice.model.OrderRequest;
import com.myecommerce.orderservice.model.OrderResponse;
import com.myecommerce.orderservice.service.OrderService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/order")
@Log4j2
public class OrderController {

    @Autowired
    OrderService orderService;

    @PostMapping("/placeOrder")
    ResponseEntity<Long> placeOrder(@RequestBody OrderRequest orderRequest){
        Long orderId = orderService.placeOrder(orderRequest);
        log.info("Order Id: {}",orderId);
        return new ResponseEntity<>(orderId, HttpStatus.CREATED);
    }

    @GetMapping("{orderId}")
    ResponseEntity<OrderResponse> getOrderById(@PathVariable long orderId){
        OrderResponse orderResponse = orderService.getOrderById(orderId);
        return new ResponseEntity<>(orderResponse, HttpStatus.OK);
    }
}
