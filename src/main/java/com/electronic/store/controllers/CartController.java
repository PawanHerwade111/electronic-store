package com.electronic.store.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.electronic.store.dtos.AddItemToCartRequest;
import com.electronic.store.dtos.ApiResponseMessage;
import com.electronic.store.dtos.CartDto;
import com.electronic.store.services.CartService;

@RestController
@RequestMapping("/carts")
public class CartController {

	@Autowired
	private CartService cartService;

	// add items to cart
	@PostMapping("/add/{userId}")
	public ResponseEntity<CartDto> addItemToCart(@RequestBody AddItemToCartRequest request,
			@PathVariable("userId") String userId) {
		CartDto dto = cartService.addItemsToCart(userId, request);
		return new ResponseEntity<>(dto, HttpStatus.OK);

	}

	// remove item from cart
	@DeleteMapping("/{userId}/items/{itemId}")
	public ResponseEntity<ApiResponseMessage> removeItemFromCart(@PathVariable("userId") String userId,
			@PathVariable("itemId") int itemId) {
		cartService.removeItemFromCart(userId, itemId);
		ApiResponseMessage reseponse = ApiResponseMessage.builder().message("Item has removed!").success(true)
				.status(HttpStatus.OK).build();
		return new ResponseEntity<>(reseponse, HttpStatus.OK);

	}

	// clear cart
	@DeleteMapping("/clear/{userId}")
	public ResponseEntity<ApiResponseMessage> removeItemFromCart(@PathVariable("userId") String userId) {
		cartService.clearCart(userId);
		ApiResponseMessage reseponse = ApiResponseMessage.builder().message("Cart is empty!").success(true)
				.status(HttpStatus.OK).build();
		return new ResponseEntity<>(reseponse, HttpStatus.OK);

	}

	// get cart by user
	@GetMapping("/{userId}")
	public ResponseEntity<CartDto> addItemToCart(@PathVariable("userId") String userId) {
		CartDto dto = cartService.getCartByUser(userId);
		return new ResponseEntity<>(dto, HttpStatus.OK);

	}
}
