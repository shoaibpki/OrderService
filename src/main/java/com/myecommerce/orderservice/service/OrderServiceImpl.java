package com.myecommerce.orderservice.service;

import com.myecommerce.orderservice.entity.Order;
import com.myecommerce.orderservice.entity.OrderDetails;
import com.myecommerce.orderservice.exception.CustomException;
import com.myecommerce.orderservice.external.client.PaymentService;
import com.myecommerce.orderservice.external.client.ProductService;
import com.myecommerce.orderservice.external.request.PaymentRequest;
import com.myecommerce.orderservice.model.OrderItemList;
import com.myecommerce.orderservice.model.OrderRequest;
import com.myecommerce.orderservice.model.OrderResponse;
import com.myecommerce.orderservice.model.ProductDetail;
import com.myecommerce.orderservice.repository.OrderRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.Instant;
import java.util.stream.Collectors;

@Service
@Log4j2
public class OrderServiceImpl implements OrderService{

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    ProductService productService;

    @Autowired
    PaymentService paymentService;

    @Autowired
    RestTemplate restTemplate;

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

        log.info("Calling payment service..");
        try
        {
            paymentService.doPayment(
                    PaymentRequest.builder()
                            .orderId(order.getOrderId())
                            .paymentMode(orderRequest.getPaymentMode())
                            .amount(orderRequest.getTotalAmount())
                            .paymentDate(Instant.now())
                            .build()
            );
            order.setStatus("PLACED");

        }catch (Exception e){
            log.error("Error occurred in payment, changing order status");
            order.setStatus("PAYMENT_FAILED");
        }
        orderRepository.save(order);
        log.info("Order placed Successfully with order id : {}", order.getOrderId());
        return order.getOrderId();
    }

    @Override
    public OrderResponse getOrderById(long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new CustomException(
                        "ORDER_NOT_FOUND",
                        "Order not found for the order id:"+orderId,
                        404));
        OrderResponse orderResponse = OrderResponse.builder()
                .orderId(order.getOrderId())
                .orderDate(order.getOrderDate())
                .orderStatus(order.getStatus())
                .totalAmount(order.getTotalAmount())
                .orderItemLists(order.getOrderDetails()
                        .stream().map(this::getOrderItems)
                        .collect(Collectors.toList()))
                .build();

        return orderResponse;
    }

    private OrderItemList getOrderItems(OrderDetails orderDetails) {
        log.info("Calling Product service to get product by product id: {}",
                orderDetails.getProductId());
        ProductDetail productDetail =
               restTemplate.getForObject(
                       "http://PRODUCT-SERVICE/product/"+orderDetails.getProductId(),
                       ProductDetail.class);
        return OrderItemList.builder()
                .productId(orderDetails.getProductId())
                .price(orderDetails.getPrice())
                .productName(productDetail.getProductName())
                .quantity(orderDetails.getQuantity())
                .build();
    }

    private OrderDetails getItems(OrderItemList orderItemList) {
        log.info("Call product service to reduce quantity of product id");
        productService.reduceQuantity(
                orderItemList.getProductId(),
                orderItemList.getQuantity());

        log.info("save order item : {}", orderItemList);
        return OrderDetails.builder()
                .productId(orderItemList.getProductId())
                .quantity(orderItemList.getQuantity())
                .price(orderItemList.getPrice())
                .build();
    }


}
