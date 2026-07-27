package com.wajeed.ecommerce.service;

import com.wajeed.ecommerce.dto.OrderItemResponse;
import com.wajeed.ecommerce.dto.OrderResponseDto;
import com.wajeed.ecommerce.exception.CartNotFoundException;
import com.wajeed.ecommerce.exception.InsufficientStockException;
import com.wajeed.ecommerce.exception.OrderNotFoundException;
import com.wajeed.ecommerce.exception.ProductNotFoundException;
import com.wajeed.ecommerce.model.*;
import com.wajeed.ecommerce.repository.*;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class OrderServiceImp implements OrderService {

    private final OrderRepo orderRepo;
    private final CartRepository cartRepository;
    private final ProductRepository productRepository;
    private final UserRepo userRepo;
    private final IdempotencyKeyRepo idempotencyKeyRepo;

    public OrderServiceImp(OrderRepo orderRepo,
                           CartRepository cartRepository,
                           ProductRepository productRepository,
                           UserRepo userRepo, IdempotencyKeyRepo idempotencyKeyRepo) {
        this.orderRepo = orderRepo;
        this.cartRepository = cartRepository;
        this.productRepository = productRepository;
        this.userRepo = userRepo;
        this.idempotencyKeyRepo = idempotencyKeyRepo;
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrderResponseDto> getUserOrderHistory(Authentication authentication) {
        String email = authentication.getName();
        Users user = userRepo.findByEmail(email).orElseThrow();

        List<Order> orders = orderRepo.findByUserId(user.getId());

        if (orders.isEmpty()) {
            throw new OrderNotFoundException("There are no OrderHistory for this userId: " + user.getId());
        }

        List<OrderResponseDto> responseDtoList = new ArrayList<>();
        for (Order order : orders) {
            OrderResponseDto dto = new OrderResponseDto();
            dto.setId(order.getId());
            dto.setOrderDate(order.getOrderDate());
            dto.setStatus(order.getStatus());
            dto.setTotalPrice(order.getTotalPrice());

            List<OrderItemResponse> itemDtoList = new ArrayList<>();
            for (OrderItem items : order.getOrderItems()) {
                OrderItemResponse item = new OrderItemResponse();
                item.setId(items.getId());
                item.setProductId(items.getProduct().getId());
                item.setProductName(items.getProductName());
                item.setQuantity(items.getQuantity());
                item.setPriceAtPurchase(items.getPriceAtPurchase());

                itemDtoList.add(item);
            }

            dto.setOrderItems(itemDtoList);
            responseDtoList.add(dto);
        }
        return responseDtoList;
    }

    @Override
    @Transactional
    public OrderResponseDto placeAnOrder(Authentication authentication, String idempotencyKey)
    {
        String email = authentication.getName();
        Users user = userRepo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found: " + email));

        Optional<IdempotencyKey> existingKey =
                idempotencyKeyRepo.findById(idempotencyKey);

        if(existingKey.isPresent()){
           Order order = existingKey.get().getOrder();

           if(existingKey.get().getUsers().getId().equals(user.getId()))
           {
               return createResponseDto(order);
           }
            throw new RuntimeException(
                    "This idempotency key belongs to another user");
        }

        Cart cart = cartRepository.findByUserId(user.getId())
                .orElseThrow(() -> new CartNotFoundException("Cart not found for this User"));

        if (cart.getCartItems() == null || cart.getCartItems().isEmpty()) {
            throw new CartNotFoundException("Order can't be placed with an empty cart");
        }

        BigDecimal totalOrderPrice = BigDecimal.ZERO;
        List<OrderItem> orderItemList = new ArrayList<>();
        Order order = new Order();

        for (CartItem cartItem : cart.getCartItems()) {

               Product product =  productRepository.findByIdWithLock(cartItem.getProduct().getId())
                       .orElseThrow(() -> new ProductNotFoundException("Product not found"));

            if (product.getStockQuantity() < cartItem.getQuantity()) {
                throw new InsufficientStockException("Insufficient stock for product: " + product.getName());
            }


            product.setStockQuantity(product.getStockQuantity() - cartItem.getQuantity());
            productRepository.save(product);


            OrderItem orderItem = new OrderItem();
            orderItem.setProductName(product.getName());
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setPriceAtPurchase(product.getPrice());
            orderItem.setProduct(product);
            orderItem.setOrder(order);
            orderItemList.add(orderItem);


            BigDecimal itemTotalPrice = product.getPrice()
                    .multiply(BigDecimal.valueOf(cartItem.getQuantity()));
            totalOrderPrice = totalOrderPrice.add(itemTotalPrice);
        }


        order.setOrderItems(orderItemList);
        order.setTotalPrice(totalOrderPrice);
        order.setUser(user);
        order.setOrderDate(LocalDateTime.now());
        order.setStatus(OrderStatus.PLACED);

        Order savedOrder = orderRepo.save(order);

        cart.getCartItems().clear();
        cart.setPrice(BigDecimal.ZERO);
        cartRepository.save(cart);


        IdempotencyKey ik = new IdempotencyKey();
        ik.setOrder(savedOrder);
        ik.setIdempotencyKey(idempotencyKey);
        ik.setUsers(user);
        idempotencyKeyRepo.save(ik);

       return createResponseDto(savedOrder);
    }

    public OrderResponseDto createResponseDto(Order order)
    {

        OrderResponseDto dto = new OrderResponseDto();
        dto.setId(order.getId());
        dto.setTotalPrice(order.getTotalPrice());
        dto.setOrderDate(order.getOrderDate());
        dto.setStatus(order.getStatus());

        List<OrderItemResponse> itemResponses = new ArrayList<>();
        for (OrderItem item : order.getOrderItems()) {
            OrderItemResponse itemResponse = new OrderItemResponse();
            itemResponse.setId(item.getId());
            itemResponse.setProductId(item.getProduct().getId());
            itemResponse.setProductName(item.getProductName());
            itemResponse.setQuantity(item.getQuantity());
            itemResponse.setPriceAtPurchase(item.getPriceAtPurchase());

            itemResponses.add(itemResponse);
        }
        dto.setOrderItems(itemResponses);

        return dto;
    }
}