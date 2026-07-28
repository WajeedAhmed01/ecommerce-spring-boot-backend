package com.wajeed.ecommerce.service;

import com.wajeed.ecommerce.dto.CartItemResponse;
import com.wajeed.ecommerce.dto.CartRequest;
import com.wajeed.ecommerce.dto.CartResponse;
import com.wajeed.ecommerce.exception.CartNotFoundException;
import com.wajeed.ecommerce.exception.InsufficientStockException;
import com.wajeed.ecommerce.exception.ProductNotFoundException;
import com.wajeed.ecommerce.model.Cart;
import com.wajeed.ecommerce.model.CartItem;
import com.wajeed.ecommerce.model.Product;
import com.wajeed.ecommerce.model.Users;
import com.wajeed.ecommerce.repository.CartItemRepo;
import com.wajeed.ecommerce.repository.CartRepository;
import com.wajeed.ecommerce.repository.ProductRepository;
import com.wajeed.ecommerce.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class CartServiceImp implements CartService {

    private final CartRepository cartRepository;
    private final CartItemRepo cartItemRepo;
    private final ProductRepository productRepository;
    private final UserRepo userRepo;

    @Autowired
    public CartServiceImp(CartRepository cartRepository,
                          CartItemRepo cartItemRepo,
                          ProductRepository productRepository,
                          UserRepo userRepo) {
        this.cartRepository = cartRepository;
        this.cartItemRepo = cartItemRepo;
        this.productRepository = productRepository;
        this.userRepo = userRepo;
    }

    @Override
    @Transactional
    public void addProductToCart(Authentication authentication, CartRequest cartRequest) {

        String email = authentication.getName();

        Users user = userRepo.findByEmail(email).orElseThrow();

        Cart cart = cartRepository.findByUserId(user.getId())
                .orElseThrow(() -> new CartNotFoundException(
                        "Cart not found for user ID: " + user.getId()));

        Product product = productRepository.findById(cartRequest.getProductId())
                .orElseThrow(() -> new ProductNotFoundException(
                        "Product not found with ID: " + cartRequest.getProductId()));

        CartItem existingItem = null;

        for (CartItem item : cart.getCartItems()) {
            if (item.getProduct().getId().equals(product.getId())) {
                existingItem = item;
                break;
            }
        }

        if (existingItem != null) {

            int totalQuantity = existingItem.getQuantity() + cartRequest.getQuantity();

            if (totalQuantity > product.getStockQuantity()) {
                throw new InsufficientStockException("Insufficient Stock");
            }

            existingItem.setQuantity(totalQuantity);

        } else {

            if (cartRequest.getQuantity() > product.getStockQuantity()) {
                throw new InsufficientStockException("Insufficient Stock");
            }

            CartItem newItem = new CartItem();
            newItem.setProduct(product);
            newItem.setQuantity(cartRequest.getQuantity());
            newItem.setCart(cart);

            cart.getCartItems().add(newItem);
        }

        BigDecimal finalCartPrice = BigDecimal.ZERO;

        for (CartItem item : cart.getCartItems()) {
            BigDecimal itemPrice = item.getProduct().getPrice();
            Integer itemQuantity = item.getQuantity();

            finalCartPrice = finalCartPrice.add(
                    itemPrice.multiply(BigDecimal.valueOf(itemQuantity))
            );
        }

        cart.setPrice(finalCartPrice);

        cartRepository.save(cart);
    }

    public CartResponse viewCart(Authentication authentication)
    {
       String email =  authentication.getName();

      Users user = userRepo.findByEmail(email).orElseThrow(()-> new UsernameNotFoundException
              ("User Not Found")
      );
      Cart cart = cartRepository.findByUserId(user.getId()).orElseThrow(()-> new CartNotFoundException(
              "cart not found"
      ));
        CartResponse cartResponse = new CartResponse();
        cartResponse.setCartId(cart.getId());
        cartResponse.setUserID(cart.getUser().getId());
        cartResponse.setTotalPrice(cart.getPrice());

        List<CartItemResponse> cartItemResponseList = new ArrayList<>();

        for(CartItem item : cart.getCartItems())
        {
            CartItemResponse cartItemResponse = new CartItemResponse();
            cartItemResponse.setCartItemId(item.getId());
            cartItemResponse.setProductId(item.getProduct().getId());
            cartItemResponse.setProductName(item.getProduct().getName());
            cartItemResponse.setPrice(item.getProduct().getPrice());
            cartItemResponse.setQuantity(item.getQuantity());

            BigDecimal subTotal = item.getProduct()
                    .getPrice()
                    .multiply(BigDecimal.valueOf(item.getQuantity()));

            cartItemResponse.setSubtotal(subTotal);

            cartItemResponseList.add(cartItemResponse);
        }
        cartResponse.setCartItemResponse(cartItemResponseList);

        return cartResponse;
    }
}