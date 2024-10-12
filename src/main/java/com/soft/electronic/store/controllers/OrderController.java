package com.soft.electronic.store.controllers;

import com.soft.electronic.store.dtos.ApiResponseMessage;
import com.soft.electronic.store.dtos.CreateOrderReq;
import com.soft.electronic.store.dtos.OrderDto;
import com.soft.electronic.store.dtos.PageableResponse;
import com.soft.electronic.store.services.OrderService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrderController {
    @Autowired
    private OrderService orderService;

    //create orders
    @PostMapping
    public ResponseEntity<OrderDto> createOrder(@Valid @RequestBody CreateOrderReq createOrderReq) {
        OrderDto orderDto = orderService.createOrder(createOrderReq);
        return new ResponseEntity<>(orderDto, HttpStatus.CREATED);
    }

    //remove order
    @DeleteMapping("/{orderId}")
    public ResponseEntity<ApiResponseMessage> removeOrder(@PathVariable String orderId) {
        orderService.removeOrder(orderId);
        ApiResponseMessage orderIsRemoved = ApiResponseMessage.builder().status(HttpStatus.OK).message("Order is removed").success(true).build();
        return new ResponseEntity<>(orderIsRemoved, HttpStatus.OK);
    }

    //get orders of user
    @GetMapping("/users/{userId}")
    public ResponseEntity<List<OrderDto>> getOrdersOfUser(@PathVariable String userId) {
        List<OrderDto> ordersOfuser = orderService.getOrdersOfuser(userId);
        return new ResponseEntity<>(ordersOfuser, HttpStatus.OK);
    }

    //get orders with pagination
    @GetMapping
    public ResponseEntity<PageableResponse<OrderDto>> getOrders(
            @RequestParam(value = "pageNumber", defaultValue = "0", required = false) int pageNumber,
            @RequestParam(value = "pageSize", defaultValue = "100", required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = "orderDate", required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = "asc", required = false) String sortDir) {
        PageableResponse<OrderDto> orders = orderService.getOrders(pageNumber,pageSize,sortBy,sortDir);
        return new ResponseEntity<>(orders, HttpStatus.OK);
    }
    @PutMapping("/update/{orderId}")
    public ResponseEntity updateOrder(@RequestBody OrderDto orderDto, @PathVariable String orderId){
        OrderDto order = orderService.updateOrder(orderDto, orderId);
        return new ResponseEntity<>(order, HttpStatus.OK);
    }
}
