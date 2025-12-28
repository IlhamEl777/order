package com.learning.order.model.dto;

import lombok.Data;

@Data
public class OrderEvent {
    private String orderId;
    private String itemId;
    private int qty;
}
