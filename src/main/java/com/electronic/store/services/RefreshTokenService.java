package com.electronic.store.services;

import com.electronic.store.dtos.RefreshTokenDto;
import com.electronic.store.dtos.UserDto;

public interface RefreshTokenService {

	// create
	RefreshTokenDto createRefreshToken(String userName);

	// find by Token
	RefreshTokenDto findByToken(String token);

	// verify
	RefreshTokenDto verifyRefreshToken(RefreshTokenDto refreshTokenDto);

	// get user details
	UserDto getUser(RefreshTokenDto refreshTokenDto);

}
