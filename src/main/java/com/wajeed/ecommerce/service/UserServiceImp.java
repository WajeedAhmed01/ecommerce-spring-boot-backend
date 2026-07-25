package com.wajeed.ecommerce.service;

import com.wajeed.ecommerce.dto.UserLoginRequest;
import com.wajeed.ecommerce.dto.UserLoginResponse;
import com.wajeed.ecommerce.dto.UserRegisterationResponse;
import com.wajeed.ecommerce.dto.UserRegistrationRequest;
import com.wajeed.ecommerce.exception.EmailAlreadyExistsException;
import com.wajeed.ecommerce.exception.InvalidEmailException;
import com.wajeed.ecommerce.model.Cart;
import com.wajeed.ecommerce.model.Users;
import com.wajeed.ecommerce.repository.CartRepository;
import com.wajeed.ecommerce.repository.UserRepo;
import com.wajeed.ecommerce.security.JwtService;
import com.wajeed.ecommerce.security.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;

@Service
public class UserServiceImp implements UserService
{
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtService jwtService;

    public  UserRegisterationResponse userRegisteration(UserRegistrationRequest userRegistrationRequest)
    {
       Optional<Users> existUser = userRepo.findByEmail(userRegistrationRequest.getEmail());

       if(existUser.isEmpty()) {
           Users user = new Users();
           user.setEmail(userRegistrationRequest.getEmail());
           user.setName(userRegistrationRequest.getName());
           user.setRole(Role.USER);
           String hashedPassword = bCryptPasswordEncoder.encode(userRegistrationRequest.getPassword());
           user.setPassword(hashedPassword);

           Users userId = userRepo.save(user);

           Cart cart = new Cart();
           cart.setPrice(BigDecimal.ZERO);
           cart.setUser(userId);


           cartRepository.save(cart);

           UserRegisterationResponse userRegisterationResponse = new UserRegisterationResponse();
           userRegisterationResponse.setName(userRegistrationRequest.getName());
           userRegisterationResponse.setEmail(userRegistrationRequest.getEmail());

           return userRegisterationResponse;
       }
       else
       {
           throw new EmailAlreadyExistsException("Email Already exists");
       }
    }
    public UserLoginResponse login(UserLoginRequest userLoginRequest)
    {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userLoginRequest.getEmail(),
                userLoginRequest.getPassword()));


        Users users = userRepo.findByEmail(userLoginRequest.getEmail())
                .orElseThrow(()-> new InvalidEmailException("Email or Password is invalid"));

        String token = jwtService.createToken(users);

        UserLoginResponse userLoginResponse = new UserLoginResponse();

          userLoginResponse.setEmail(users.getEmail());
          userLoginResponse.setName(users.getName());
          userLoginResponse.setMessage("Logged in successfully");
          userLoginResponse.setToken(token);

     return userLoginResponse;

    }
}
