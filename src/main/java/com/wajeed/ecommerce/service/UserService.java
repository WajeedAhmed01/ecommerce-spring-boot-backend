package com.wajeed.ecommerce.service;

import com.wajeed.ecommerce.dto.UserLoginRequest;
import com.wajeed.ecommerce.dto.UserLoginResponse;
import com.wajeed.ecommerce.dto.UserRegisterationResponse;
import com.wajeed.ecommerce.dto.UserRegistrationRequest;

public interface UserService
{
  public UserRegisterationResponse userRegisteration(UserRegistrationRequest userRegistrationRequest);
  public UserLoginResponse login(UserLoginRequest userLoginRequest);
}
