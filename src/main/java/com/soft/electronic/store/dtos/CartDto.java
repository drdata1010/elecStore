package com.soft.electronic.store.dtos;

import com.soft.electronic.store.entities.CartItems;
import com.soft.electronic.store.entities.User;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class CartDto {
    private String cartId;
    private Date createdAt;
    private UserDTO user;

    //mapping cart items
    private List<CartItemDto> items = new ArrayList<>();
}
