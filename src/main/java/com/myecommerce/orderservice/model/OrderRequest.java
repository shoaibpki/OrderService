package com.myecommerce.orderservice.model;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class OrderRequest {

//    private String orderStatus;
    private PaymentMode paymentMode;
    private long totalAmount;
    private List<OrderItemList> orderItemList;
}
