package com.mukesh.order.controller;

import com.mukesh.order.dto.CreateOrderRequest;
import com.mukesh.order.dto.OrderResponse;

@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    public OrderResponse createOrder(@RequestBody CreateOrderRequest request){
        return orderService.createOrder(request);

    }

}
