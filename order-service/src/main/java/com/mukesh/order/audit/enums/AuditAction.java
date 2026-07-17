package com.mukesh.order.audit.enums;

public enum AuditAction {

    // Authentication
    LOGIN_SUCCESS,
    LOGIN_FAILED,
    TOKEN_REFRESHED,
    LOGOUT,
    LOGOUT_ALL,
    SESSION_REVOKED,

    // User
    USER_CREATED,
    USER_UPDATED,
    USER_DELETED,
    ROLE_ASSIGNED,
    PASSWORD_CHANGED,

    // Order
    ORDER_CREATED,
    ORDER_UPDATED,
    ORDER_CANCELLED,

    // Inventory
    INVENTORY_RESERVED,
    INVENTORY_RELEASED,
    INVENTORY_UPDATED,

    // Payment
    PAYMENT_CREATED,
    PAYMENT_COMPLETED,
    PAYMENT_FAILED,
    PAYMENT_REFUNDED

}