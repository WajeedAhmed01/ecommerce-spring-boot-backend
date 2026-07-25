package com.wajeed.ecommerce.dto;

import lombok.Data;

@Data
public class UserLoginResponse
{
    private String name;
    private String email;
    private String message;
    private String token;
}
