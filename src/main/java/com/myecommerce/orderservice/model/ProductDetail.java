package com.myecommerce.orderservice.model;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductDetail {
    private long productId;
    private String productName;
    private long price;
    private long quantity;
}
