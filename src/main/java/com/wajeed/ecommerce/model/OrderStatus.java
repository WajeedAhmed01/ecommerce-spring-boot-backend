package com.wajeed.ecommerce.model;

public enum OrderStatus
{
    PENDING,          // Order created, awaiting payment confirmation
    PLACED,           // Payment confirmed, order successfully placed
    PROCESSING,       // Order is being packed in the warehouse
    SHIPPED,          // Order handed over to courier / in transit
    DELIVERED,        // Successfully delivered to customer
    CANCELLED,        // Cancelled by customer or system
    REFUNDED          // Order returned and payment refunded
}
