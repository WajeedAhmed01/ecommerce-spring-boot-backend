package com.wajeed.ecommerce.exception;

public class SkuAlreadyExistException extends RuntimeException{
    public SkuAlreadyExistException(String message)
    {
        super(message);
    }
}
