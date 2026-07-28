package com.wajeed.ecommerce.exception;

public class InvalidIdempotencyKeyException extends RuntimeException
{
    public InvalidIdempotencyKeyException(String message) {
        super(message);
    }
}
