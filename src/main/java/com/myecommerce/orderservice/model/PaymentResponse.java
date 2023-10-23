package com.myecommerce.orderservice.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PaymentResponse {
    private long paymentId;
    private long orderId;
    private String referenceNumber;
    private PaymentMode paymentMode;
    private Instant paymentDate;
    private String paymentStatus;
    private long amount;
}
