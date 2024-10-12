package com.soft.electronic.store.services.impl;

import com.soft.electronic.store.dtos.CreateOrderReq;
import com.soft.electronic.store.dtos.OrderDto;
import com.soft.electronic.store.dtos.PageableResponse;
import com.soft.electronic.store.entities.*;
import com.soft.electronic.store.exceptions.BadApiRequestException;
import com.soft.electronic.store.exceptions.ResourceNotFound;
import com.soft.electronic.store.helper.Helper;
import com.soft.electronic.store.repositories.CartRepository;
import com.soft.electronic.store.repositories.OrderRepository;
import com.soft.electronic.store.repositories.UserRepository;
import com.soft.electronic.store.services.OrderService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
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
    public OrderDto createOrder(CreateOrderReq orderDto) {

        //fetch user
        User user = userRepository.findById(orderDto.getUserId()).orElseThrow(() -> new ResourceNotFound("User not found with this user id!"));
        //fetch cart
        Cart cart = cartRepository.findById(orderDto.getCartId()).orElseThrow(() -> new ResourceNotFound("Cart not found with this cart id!"));
        List<CartItems> cartItems = cart.getItems();
        if(cartItems.size() <= 0) {
            throw new BadApiRequestException("Invalid number of items in cart !!");
        }

        //other checks

        Order order = Order.builder()
                .billingName(orderDto.getBillingName())
                .billingAddress(orderDto.getBillingAddress())
                .billingPhone(orderDto.getBillingPhone())
                .orderDate(new Date())
                .deliveredDate(null)
                .paymentStatus(orderDto.getPaymentStatus())
                .orderStatus(orderDto.getOrderStatus())
                .orderId(UUID.randomUUID().toString())
                .user(user)
                .build();

        //order items and amount is not set
        AtomicReference<Integer> orderAmount = new AtomicReference<>(0);
        List<OrderItem> orderItems = cartItems.stream().map(cartItem -> {
//            CartItem->OrderItem
            OrderItem orderItem = OrderItem.builder()
                    .quantity(cartItem.getQuantity())
                    .product(cartItem.getProduct())
                    .totalPrice(cartItem.getQuantity() * cartItem.getProduct().getDiscountedPrice())
                    .order(order)
                    .build();
            orderAmount.set(orderAmount.get() + orderItem.getTotalPrice());
            return orderItem;
        }).collect(Collectors.toList());
        order.setOrderItems(orderItems);
        order.setOrderAmount(orderAmount.get());
        cart.getItems().clear();
        cartRepository.save(cart);
        Order savedOrder = orderRepository.save(order);

        return modelMapper.map(savedOrder, OrderDto.class) ;
    }

    @Override
    public void removeOrder(String orderId) {

        Order order = orderRepository.findById(orderId).orElseThrow(() -> new ResourceNotFound("Order not found with given ID!!"));
        orderRepository.delete(order);
    }

    @Override
    public List<OrderDto> getOrdersOfuser(String userid) {
        User user = userRepository.findById(userid).orElseThrow(() -> new ResourceNotFound("User not found with User ID! "));
        List<Order> orders = orderRepository.findByUser(user);
        List<OrderDto> orderDtos = orders.stream().map(order -> modelMapper.map(order, OrderDto.class)).collect(Collectors.toList());
        return orderDtos;
    }

    @Override
    public PageableResponse<OrderDto> getOrders(int pageNumber, int pageSize, String sortBy, String sortDir) {

        Sort sort = (sortDir.equalsIgnoreCase("desc"))?(Sort.by(sortBy).descending()):(Sort.by(sortBy).ascending());
        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
        Page<Order> page = orderRepository.findAll(pageable);
        return Helper.getPageableResponse(page, OrderDto.class);
    }

    @Override
    public OrderDto updateOrder(OrderDto orderDto, String orderId) {
        Order order = orderRepository.findById(orderId).orElseThrow(() -> new ResourceNotFound("Order not found!!"));
        order.setDeliveredDate(new Date());
        order.setPaymentStatus(orderDto.getPaymentStatus());
        order.setOrderStatus(orderDto.getOrderStatus());
        Order updatedOrder = orderRepository.save(order);
        return modelMapper.map(updatedOrder, OrderDto.class);
    }
}
