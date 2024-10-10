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
@ToString
@Builder
@Entity
@Table(name="cart")
public class Cart {
    //which cart is being owned by which user
    @Id
    private String cartId;
    private Date createdAt;
    @OneToOne
    private User user;

    //mapping cart items
    @OneToMany(mappedBy = "cart",cascade = CascadeType.ALL, fetch = FetchType.EAGER,orphanRemoval = true)
    private List<CartItems> items = new ArrayList<>();

}
