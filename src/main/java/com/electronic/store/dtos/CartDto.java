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
public class CartDto {

	private String cartId;

	private Date createdAt;

	private User user;

	private List<CartItemDto> items = new ArrayList<>();
}
