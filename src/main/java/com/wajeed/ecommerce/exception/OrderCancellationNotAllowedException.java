package com.wajeed.ecommerce.exception;

public class OrderCancellationNotAllowedException extends  RuntimeException
{
    public OrderCancellationNotAllowedException(String message) {
        super(message);
    }
}
