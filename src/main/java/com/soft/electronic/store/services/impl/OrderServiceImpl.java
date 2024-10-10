package com.soft.electronic.store.services.impl;

import com.soft.electronic.store.dtos.OrderDto;
import com.soft.electronic.store.dtos.PageableResponse;
import com.soft.electronic.store.entities.*;
import com.soft.electronic.store.exceptions.BadApiRequestException;
import com.soft.electronic.store.exceptions.ResourceNotFound;
import com.soft.electronic.store.repositories.CartRepository;
import com.soft.electronic.store.repositories.OrderRepository;
import com.soft.electronic.store.repositories.UserRepository;
import com.soft.electronic.store.services.OrderService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private CartRepository cartRepository;
    @Override
    public OrderDto createOrder(OrderDto orderDto, String userId, String cartId) {

        //fetch user
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFound("User not found with this user id!"));
        //fetch cart
        Cart cart = cartRepository.findById(cartId).orElseThrow(() -> new ResourceNotFound("Cart not found with this cart id!"));
        List<CartItems> cartItems = cart.getItems();
        if(cartItems.size() <= 0) {
            throw new BadApiRequestException("Invalid number of items in cart !!");
        }

        //other checks

        Order.builder()
                .billingName(orderDto.getBillingName())
                .billingAddress(orderDto.getBillingAddress())
                .billingPhone(orderDto.getBillingPhone())
                .orderDate(new Date())
                .deliveredDate(orderDto.getDeliveredDate())
                .paymentStatus(orderDto.getPaymentStatus())
                .orderStatus(orderDto.getOrderStatus())
                .orderId(UUID.randomUUID().toString())
                .user(user)
                .build();

        //order items and amount is not set
        List<OrderItem> orderItems = cartItems.stream().map(cartItem -> {
//            CartItem->OrderItem
            OrderItem orderItem = OrderItem.builder()
                    .quantity(cartItem.getQuantity())
                    .product(cartItem.getProduct())
                    .totalPrice(cartItem.getQuantity() * cartItem.getProduct().getDiscountedPrice())
                    .order(order)
                    .build();
            return orderItem;
        }).collect(Collectors.toList());
        order.setOrderItems

        return null;
    }

    @Override
    public void removeOrder(String orderId) {

    }

    @Override
    public List<OrderDto> getOrdersOfuser(String userid) {
        return null;
    }

    @Override
    public PageableResponse<OrderDto> getOrders(int pageNumber, int pageSize, String sortBy, String sortDir) {
        return null;
    }
}
