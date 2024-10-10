package com.soft.electronic.store.repositories;

import com.soft.electronic.store.entities.CartItems;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartItemRepository extends JpaRepository<CartItems,Integer> {
}
