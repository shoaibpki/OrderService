package com.myecommerce.orderservice.model;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class OrderItemList {

    private long productId;
    private long quantity;
    private long price;

}
