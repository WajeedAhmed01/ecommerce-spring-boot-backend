package com.wajeed.ecommerce.controller;

import com.wajeed.ecommerce.dto.UserLoginRequest;
import com.wajeed.ecommerce.dto.UserLoginResponse;
import com.wajeed.ecommerce.dto.UserRegisterationResponse;
import com.wajeed.ecommerce.dto.UserRegistrationRequest;
import com.wajeed.ecommerce.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
public class UserController
{
    UserService userService;

    public UserController(UserService userService)
    {
        this.userService = userService;
    }
    @PostMapping("/register")
    public ResponseEntity<UserRegisterationResponse> userRegister(@Valid @RequestBody UserRegistrationRequest userDto)
    {
        UserRegisterationResponse userRegisterationResponse = userService.userRegisteration(userDto);
        return new ResponseEntity<>(userRegisterationResponse, HttpStatus.CREATED);
    }
    @PostMapping("/login")
    public ResponseEntity<UserLoginResponse> userLogin(@RequestBody UserLoginRequest userLoginRequest)
    {
        UserLoginResponse login = userService.login(userLoginRequest);

        return new ResponseEntity<>(login , HttpStatus.OK);
    }
}
