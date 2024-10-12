package com.soft.electronic.store.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@Table(name="orders")
public class Order {
    @Id
    private String orderId;

    private String orderStatus;//PENDING,DISPATCHED,DELIVERED -- can also use enum

    private String paymentStatus; //NOT-PAID,PAID || boolean || enum

    private int orderAmount;

    @Column(length = 1000)
    private String billingAddress;

    private String billingPhone;

    private String billingName;

    private Date orderDate;

    private Date deliveredDate;
    //user
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User user;
    @OneToMany(mappedBy = "order", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<OrderItem> orderItems = new ArrayList<>();
    //fetch type EAGER is used so that when we fetch order we will get user info as well, if we dont want this to happen then we should use LAZY



}
