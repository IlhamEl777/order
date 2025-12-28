package com.learning.order.controller;

import com.learning.order.model.dto.OrderEvent;
import com.learning.order.service.OrderService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/orders")
public class OrderController {
    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public String placeOrder(@RequestBody OrderEvent order){
        orderService.createOrder(order);
        return "Order berhasil dibuat, sedang diproses!";
    }

}
