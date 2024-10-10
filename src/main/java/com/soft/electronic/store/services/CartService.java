package com.soft.electronic.store.services;

import com.soft.electronic.store.dtos.AddItemToCartRequest;
import com.soft.electronic.store.dtos.CartDto;

public interface CartService {
    //add Items:
    //case1: if cart is not available for user : we will create the cart and add items
    //case2: if cart available : simply add items to cart
    CartDto addItemToCart(String userId, AddItemToCartRequest request);

    //remove item from cart
    void removeItemFromCart(String userId, int cartItem);

    //clear cart
    void clearCart(String userId);

    //get cart by user
    CartDto getCartByUser(String userId);
}
