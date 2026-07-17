package com.mukesh.order.entity.security;

public enum PermissionName {

    // Order
    ORDER_CREATE,
    ORDER_READ,
    ORDER_UPDATE,
    ORDER_CANCEL,
    ORDER_DELETE,

    // Inventory
    INVENTORY_VIEW,
    INVENTORY_RESERVE,
    INVENTORY_RELEASE,
    INVENTORY_UPDATE,

    // Payment
    PAYMENT_CREATE,
    PAYMENT_VIEW,
    PAYMENT_REFUND,
    PAYMENT_CANCEL,

    // Customer
    CUSTOMER_VIEW,
    CUSTOMER_UPDATE,

    // User Management
    USER_CREATE,
    USER_UPDATE,
    USER_DELETE,

    ROLE_ASSIGN,

    // Audit
    AUDIT_READ,
    AUDIT_EXPORT,

    // Monitoring
    METRICS_VIEW,
    LOGS_VIEW
}
