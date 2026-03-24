package com.electronic.store.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.electronic.store.dtos.ApiResponseMessage;
import com.electronic.store.dtos.CreateOrderRequestDto;
import com.electronic.store.dtos.OrderDto;
import com.electronic.store.dtos.PageableResponse;
import com.electronic.store.services.OrderService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/orders")
public class OrderController {

	@Autowired
	private OrderService orderService;

	// create
	@PostMapping("/createOrder")
	public ResponseEntity<OrderDto> createOrder(@Valid @RequestBody CreateOrderRequestDto requestDto) {
		OrderDto orderDto = orderService.createOrder(requestDto);
		return new ResponseEntity<>(orderDto, HttpStatus.CREATED);

	}

	// remove order
	@DeleteMapping("/removeOrder/{orderId}")
	public ResponseEntity<ApiResponseMessage> removeOrder(@PathVariable("orderId") String orderId) {
		orderService.removeOrder(orderId);
		ApiResponseMessage apiResponseMessage = ApiResponseMessage.builder().message("Order is removed!")
				.status(HttpStatus.OK).success(true).build();
		return new ResponseEntity<>(apiResponseMessage, HttpStatus.OK);
	}

	// get orders of the user
	@GetMapping("/getOrder/{userId}")
	public ResponseEntity<List<OrderDto>> getOrdersOfUser(@PathVariable("userId") String userId) {
		List<OrderDto> orderDtos = orderService.getOrdersByUser(userId);
		return new ResponseEntity<>(orderDtos, HttpStatus.OK);
	}

	// get all orders
	@GetMapping("/getAllOrders")
	public ResponseEntity<PageableResponse<OrderDto>> getOrders(
			@RequestParam(value = "pageNumber", defaultValue = "0", required = false) int pageNumber,
			@RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize,
			@RequestParam(value = "sortBy", defaultValue = "orderedDate", required = false) String sortBy,
			@RequestParam(value = "sortDir", defaultValue = "desc", required = false) String sortDir) {
		PageableResponse<OrderDto> orders = orderService.getAllOrders(pageNumber, pageSize, sortBy, sortDir);
		return new ResponseEntity<>(orders, HttpStatus.OK);
	}

	// update Order
	@PutMapping("/updateOrder")
	public ResponseEntity<OrderDto> getOrdersOfUser(@RequestBody OrderDto orderDto) {
		OrderDto updatedOrderDto = orderService.updateOrder(orderDto);
		return new ResponseEntity<>(updatedOrderDto, HttpStatus.OK);
	}

}
