package com.electronic.store.services;

import java.util.List;

import com.electronic.store.dtos.CreateOrderRequestDto;
import com.electronic.store.dtos.OrderDto;
import com.electronic.store.dtos.PageableResponse;

public interface OrderService {
	
	//create order
	OrderDto createOrder(CreateOrderRequestDto orderRequestDto);
	
	//remove order
	void removeOrder(String orderId);
	
	//get orders by user
	List<OrderDto> getOrdersByUser(String userId);
	
	//get all orders
	PageableResponse<OrderDto> getAllOrders(int pageNumber, int pageSize, String sortBy, String sortDir);
	
	//other methods
	//update Order
	OrderDto updateOrder(OrderDto requestDto);

}
