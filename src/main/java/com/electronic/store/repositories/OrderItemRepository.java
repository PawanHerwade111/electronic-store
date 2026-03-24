package com.electronic.store.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.electronic.store.entities.OrderItem;

public interface OrderItemRepository extends JpaRepository<OrderItem, Integer> {

}
