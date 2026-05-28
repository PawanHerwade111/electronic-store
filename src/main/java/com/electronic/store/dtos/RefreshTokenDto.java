package com.electronic.store.dtos;

import java.time.Instant;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class RefreshTokenDto {

	private int id;
	private String token;
	private Instant expiryDate;
	//private UserDto user;

}
