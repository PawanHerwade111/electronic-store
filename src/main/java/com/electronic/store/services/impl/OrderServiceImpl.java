package com.electronic.store.services.impl;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.electronic.store.dtos.CreateOrderRequestDto;
import com.electronic.store.dtos.OrderDto;
import com.electronic.store.dtos.PageableResponse;
import com.electronic.store.entities.Cart;
import com.electronic.store.entities.CartItem;
import com.electronic.store.entities.Order;
import com.electronic.store.entities.OrderItem;
import com.electronic.store.entities.User;
import com.electronic.store.exceptions.BadApiRequestException;
import com.electronic.store.exceptions.ResourceNotFoundException;
import com.electronic.store.helper.Helper;
import com.electronic.store.repositories.CartRepository;
import com.electronic.store.repositories.OrderRepository;
import com.electronic.store.repositories.UserRepository;
import com.electronic.store.services.OrderService;

@Service
public class OrderServiceImpl implements OrderService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private OrderRepository orderRepository;

	@Autowired
	private CartRepository cartRepository;

	@Autowired
	private ModelMapper mapper;

	@Override
	public OrderDto createOrder(CreateOrderRequestDto orderRequestDto) {
		// fetch user
		String userId = orderRequestDto.getUserId();
		User user = userRepository.findById(userId)
				.orElseThrow(() -> new ResourceNotFoundException("User not found with given Id!"));
		// fetch cart
		String cartId = orderRequestDto.getCartId();
		Cart cart = cartRepository.findById(cartId)
				.orElseThrow(() -> new ResourceNotFoundException("Cart not found with given Id!"));
		List<CartItem> cartItems = cart.getItems();
		if (cartItems.size() <= 0) {
			throw new BadApiRequestException("Invalid number of items in Cart!");
		}

		Order order = Order.builder().billingName(orderRequestDto.getBillingName())
				.billingPhone(orderRequestDto.getBillingPhone()).billingAddress(orderRequestDto.getBillingAddress())
				.orderedDate(new Date()).deliveredDate(null).paymentStatus(orderRequestDto.getPaymentStatus())
				.orderStatus(orderRequestDto.getOrderStatus()).orderId(UUID.randomUUID().toString()).user(user).build();

		AtomicReference<Integer> orderAmount = new AtomicReference<>(0);
		List<OrderItem> orderItems = cartItems.stream().map(cartItem -> {
			OrderItem orderItem = OrderItem.builder().quantity(cartItem.getQuantity()).product(cartItem.getProduct())
					.totalPrice(cartItem.getQuantity() * cartItem.getProduct().getDiscountedPrice()).order(order)
					.build();
			orderAmount.set(orderAmount.get() + orderItem.getTotalPrice());
			return orderItem;
		}).collect(Collectors.toList());
		order.setOrderItems(orderItems);
		order.setOrderAmount(orderAmount.get());
		// clear cart
		cart.getItems().clear();
		cartRepository.save(cart);
		Order savedOrder = orderRepository.save(order);
		return mapper.map(savedOrder, OrderDto.class);
	}

	@Override
	public void removeOrder(String orderId) {
		Order order = orderRepository.findById(orderId)
				.orElseThrow(() -> new ResourceNotFoundException("Order not found with given Id!"));
		orderRepository.delete(order);
	}

	@Override
	public List<OrderDto> getOrdersByUser(String userId) {
		User user = userRepository.findById(userId)
				.orElseThrow(() -> new ResourceNotFoundException("User not found with given Id!"));
		List<Order> orders = orderRepository.findByUser(user);
		List<OrderDto> ordersDto = orders.stream().map(order -> mapper.map(order, OrderDto.class))
				.collect(Collectors.toList());
		return ordersDto;
	}

	@Override
	public PageableResponse<OrderDto> getAllOrders(int pageNumber, int pageSize, String sortBy, String sortDir) {
		Sort sort = (sortDir.equalsIgnoreCase("desc")) ? (Sort.by(sortBy).descending()) : (Sort.by(sortBy).ascending());
		Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
		Page<Order> page = orderRepository.findAll(pageable);
		return Helper.getPageableResponse(page, OrderDto.class);
	}

	@Override
	public OrderDto updateOrder(OrderDto requestDto) {
		Order order = orderRepository.findById(requestDto.getOrderId())
				.orElseThrow(() -> new ResourceNotFoundException("Order not found with given Id!"));
		order.setPaymentStatus(requestDto.getPaymentStatus());
		Order updatedOrder = orderRepository.save(order);
		return mapper.map(updatedOrder, OrderDto.class);
	}

}
