package com.wajeed.ecommerce.exception;

public class CartItemNotFoundException extends Exception
{
    public CartItemNotFoundException(String message)
    {
        super(message);
    }
}
