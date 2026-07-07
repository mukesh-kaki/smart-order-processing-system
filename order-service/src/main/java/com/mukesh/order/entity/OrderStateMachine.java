package com.mukesh.order.entity;

import java.util.EnumSet;
import java.util.Map;

public final class OrderStateMachine {

    private  static final Map<OrderStatus, EnumSet<OrderStatus>> VALID_TRANSITIONS=
            Map.of(OrderStatus.CREATED,
                    EnumSet.of(OrderStatus.INVENTORY_PENDING),

                    OrderStatus.INVENTORY_PENDING,
                    EnumSet.of(OrderStatus.INVENTORY_RESERVED),

                    OrderStatus.INVENTORY_RESERVED,
                    EnumSet.of(OrderStatus.PAYMENT_PENDING),

                    OrderStatus.PAYMENT_PENDING,
                    EnumSet.of(
                            OrderStatus.PAYMENT_COMPLETED,
                            OrderStatus.PAYMENT_FAILED
                    ),

                    OrderStatus.PAYMENT_COMPLETED,
                    EnumSet.of(OrderStatus.COMPLETED),

                    OrderStatus.PAYMENT_FAILED,
                    EnumSet.of(OrderStatus.CANCELLED),

                    OrderStatus.COMPLETED,
                    EnumSet.noneOf(OrderStatus.class),

                    OrderStatus.CANCELLED,
                    EnumSet.noneOf(OrderStatus.class)
            );

    private OrderStateMachine(){
    }

    public static void transition(OrderEntity order, OrderStatus newStatus){
        OrderStatus current=order.getStatus();

        if(current==null){
            order.setStatus(newStatus);
            return;
        }

        EnumSet<OrderStatus> allowed=VALID_TRANSITIONS.get(current);
        if(!allowed.contains(newStatus)){
            throw new IllegalStateException("Invalid order state transition: " + current +"->" + newStatus);
        }
        order.setStatus(newStatus);
    }
}
