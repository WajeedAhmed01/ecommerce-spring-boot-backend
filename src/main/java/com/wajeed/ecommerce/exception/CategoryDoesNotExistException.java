package com.wajeed.ecommerce.exception;

public class CategoryDoesNotExistException extends RuntimeException{
    public CategoryDoesNotExistException(String message)
    {
        super(message);
    }
}
