package com.soft.electronic.store.services.impl;

import com.soft.electronic.store.dtos.AddItemToCartRequest;
import com.soft.electronic.store.dtos.CartDto;
import com.soft.electronic.store.entities.Cart;
import com.soft.electronic.store.entities.CartItems;
import com.soft.electronic.store.entities.Product;
import com.soft.electronic.store.entities.User;
import com.soft.electronic.store.exceptions.BadApiRequestException;
import com.soft.electronic.store.exceptions.ResourceNotFound;
import com.soft.electronic.store.repositories.*;
import com.soft.electronic.store.services.CartService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@Service
public class CartServiceImpl implements CartService {
    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CartRepository cartRepository;
    @Autowired
    private ModelMapper mapper;

    @Autowired
    private CartItemRepository cartItemRepository;

    @Override
    public CartDto addItemToCart(String userId, AddItemToCartRequest request) {
        int quantity = request.getQuantity();
        String productId = request.getProductId();
        if(quantity <= 0){
            throw new BadApiRequestException("Request quantity is not valid !!");
        }
        //fetch product
        Product product = productRepository.findById(productId).orElseThrow(() -> new ResourceNotFound("Product not found in Database!!"));
        //fetch user from db
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFound("User not found in Database"));
        Cart cart = null;
        try{
            cart = cartRepository.findByUser(user).get();
        } catch (NoSuchElementException e){
            cart = new Cart();
            cart.setCartId(UUID.randomUUID().toString());
            cart.setCreatedAt(new Date());
        }
        //perform cart operations
        //if cart item already present then update
        List<CartItems> items = cart.getItems();
        AtomicReference<Boolean> updated = new AtomicReference<>(false);
        List<CartItems> updatedItems = items.stream().map(item -> {
            if (item.getProduct().getProductId().equals(productId)) {
                //item already present, update quantity
                item.setQuantity(quantity);
                item.setTotalPrice(quantity * product.getDiscountedPrice());
                updated.set(true);
            }
            return item;
        }).collect(Collectors.toList());

        cart.setItems(updatedItems);
        if(!updated.get()){
            //create items to add to cart
            CartItems cartItem = CartItems.builder()
                    .quantity(quantity)
                    .totalPrice(quantity * product.getDiscountedPrice())
                    .cart(cart)
                    .product(product)
                    .build();
            cart.getItems().add(cartItem);
        }

        cart.setUser(user);
        Cart updatedCart = cartRepository.save(cart);

        return mapper.map(updatedCart,CartDto.class);
    }

    @Override
    public void removeItemFromCart(String userId, int cartItem) {
        CartItems cartItems = cartItemRepository.findById(cartItem).orElseThrow(() -> new ResourceNotFound("Cart Item not found !!"));
        cartItemRepository.delete(cartItems);
    }

    @Override
    public void clearCart(String userId) {
        //fetch user from db
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFound("User not found in Database"));
        Cart cart = cartRepository.findByUser(user).orElseThrow(() -> new ResourceNotFound("Cart of user not found !!"));
        cart.getItems().clear();
        cartRepository.save(cart);
    }

    @Override
    public CartDto getCartByUser(String userId){
        //fetch user from db
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFound("User not found in Database"));
        Cart cart = cartRepository.findByUser(user).orElseThrow(() -> new ResourceNotFound("Cart of user not found !!"));
        return mapper.map(cart,CartDto.class);
    }
}
