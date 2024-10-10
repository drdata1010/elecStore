package com.soft.electronic.store.services;

import com.soft.electronic.store.dtos.OrderDto;
import com.soft.electronic.store.dtos.PageableResponse;
import com.soft.electronic.store.entities.User;

import java.util.List;

public interface OrderService {

    //create order
    OrderDto createOrder(OrderDto orderDto, String userId, String cartId);

    //remove order
    void removeOrder(String orderId);

    //get orders of user
    List<OrderDto> getOrdersOfuser(String userid);
    //get all orders by admin
    PageableResponse<OrderDto> getOrders(int pageNumber, int pageSize, String sortBy, String sortDir);
    //other methods related to order
}
