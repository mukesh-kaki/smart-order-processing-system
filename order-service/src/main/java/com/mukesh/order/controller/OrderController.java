package com.mukesh.order.controller;

import com.mukesh.order.dto.CreateOrderRequest;
import com.mukesh.order.dto.OrderResponse;
import com.mukesh.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    public OrderResponse createOrder(@RequestBody CreateOrderRequest request) {
        return orderService.createOrder(request);
    }

    @PostMapping
    @PreAuthorize("hasAuthority('ORDER_CREATE')")
    public String createOrder() {

        return "Order Created";

    }

    @GetMapping
    @PreAuthorize("hasAuthority('ORDER_READ')")
    public String getOrder(){
        return "Order Details";
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ORDER_UPDATE')")
    public String updateOrder() {

        return "Order Updated";

    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ORDER_DELETE')")
    public String deleteOrder() {

        return "Order Deleted";

    }
}
