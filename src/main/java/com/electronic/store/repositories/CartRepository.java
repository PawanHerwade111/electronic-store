package com.electronic.store.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.electronic.store.entities.Cart;
import com.electronic.store.entities.User;

public interface CartRepository extends JpaRepository<Cart, String> {
	
	Optional<Cart> findByUser(User user);

}
