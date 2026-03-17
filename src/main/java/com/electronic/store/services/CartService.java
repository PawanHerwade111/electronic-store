package com.electronic.store.services;

import com.electronic.store.dtos.AddItemToCartRequest;
import com.electronic.store.dtos.CartDto;

public interface CartService {

	//add items to cart
	CartDto addItemsToCart(String userId, AddItemToCartRequest request);
	
	//remove item from cart
	void removeItemFromCart(String userId, int cartItemId);
	
	//clear cart - remove all items from cart
	void clearCart(String userId);
	
	//fetch cart by user
	CartDto getCartByUser(String userId);
}
