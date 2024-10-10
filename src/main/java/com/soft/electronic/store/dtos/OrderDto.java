package com.soft.electronic.store.dtos;

import com.soft.electronic.store.entities.OrderItem;
import com.soft.electronic.store.entities.User;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class OrderDto {
    private String orderId;
    private String orderStatus = "PENDING";//PENDING,DISPATCHED,DELIVERED -- can also use enum
    private String paymentStatus = "NOTPAID"; //NOT-PAID,PAID || boolean || enum
    private int orderAmount;
    private String billingAddress;
    private String billingPhone;
    private String billingName;
    private Date orderDate = new Date();
    private Date deliveredDate;
//    private UserDto user;
    private List<OrderItemDto> orderItems = new ArrayList<>();
}
