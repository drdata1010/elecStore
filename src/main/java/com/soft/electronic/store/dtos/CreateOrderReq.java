package com.soft.electronic.store.dtos;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class CreateOrderReq {

    @NotBlank(message = "Cart ID is required!!")
    private String cartId;
    @NotBlank(message = "User ID is required!!")
    private String userId;
    private String orderStatus = "PENDING";//PENDING,DISPATCHED,DELIVERED -- can also use enum
    private String paymentStatus = "NOTPAID"; //NOT-PAID,PAID || boolean || enum
    @NotBlank(message = "Address is required!!")
    private String billingAddress;
    @NotBlank(message = "Phone is required!!")
    private String billingPhone;
    @NotBlank(message = "Billing name is required!!")
    private String billingName;
}
