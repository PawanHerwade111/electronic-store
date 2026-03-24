package com.electronic.store.dtos;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.electronic.store.entities.User;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderDto {

	private String orderId;

	// PENDING, DISPATCHED, DELIVERED
	// todo enum later
	private String orderStatus = "PENDING";

	// NOT-PAID, PAID
	// todo enum or boolean later
	private String paymentStatus = "NOTPAID";

	private int orderAmount;

	private String billingAddress;

	private String billingPhone;

	private String billingName;

	private Date orderedDate;

	private Date deliveredDate;

	//private UserDto user;

	private List<OrderItemDto> orderItems = new ArrayList<>();

}
