package com.electronic.store.dtos;

import jakarta.validation.constraints.NotBlank;
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
public class CreateOrderRequestDto {

	@NotBlank(message = "Cart Id is required!")
	private String cartId;

	@NotBlank(message = "User Id is required!")
	private String userId;
	// PENDING, DISPATCHED, DELIVERED
	// todo enum later
	private String orderStatus = "PENDING";

	// NOT-PAID, PAID
	// todo enum or boolean later
	private String paymentStatus = "NOTPAID";

	@NotBlank(message = "Address is required!")
	private String billingAddress;

	@NotBlank(message = "Phone number is required!")
	private String billingPhone;

	@NotBlank(message = "Billing name is required!")
	private String billingName;

}
