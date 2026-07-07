package com.mukesh.order.entity;

public enum OrderStatus {

    CREATED,

    INVENTORY_PENDING,

    INVENTORY_RESERVED,

    PAYMENT_PENDING,

    PAYMENT_COMPLETED,

    COMPLETED,

    PAYMENT_FAILED,

    CANCELLED

}