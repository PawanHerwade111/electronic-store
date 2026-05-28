package com.electronic.store.services.impl;

import java.time.Instant;
import java.util.UUID;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.electronic.store.dtos.RefreshTokenDto;
import com.electronic.store.dtos.UserDto;
import com.electronic.store.entities.RefreshToken;
import com.electronic.store.entities.User;
import com.electronic.store.exceptions.ResourceNotFoundException;
import com.electronic.store.repositories.RefreshTokenRepository;
import com.electronic.store.repositories.UserRepository;
import com.electronic.store.services.RefreshTokenService;

@Service
public class RefreshTokenServiceImpl implements RefreshTokenService {

	private UserRepository userRepository;

	private RefreshTokenRepository refreshTokenRepository;

	private ModelMapper modelMapper;

	public RefreshTokenServiceImpl(UserRepository userRepository, RefreshTokenRepository refreshTokenRepository,
			ModelMapper modelMapper) {
		this.userRepository = userRepository;
		this.refreshTokenRepository = refreshTokenRepository;
		this.modelMapper = modelMapper;
	}

	@Override
	public RefreshTokenDto createRefreshToken(String userName) {
		User user = userRepository.findByEmail(userName)
				.orElseThrow(() -> new ResourceNotFoundException("User not found with given userName!!"));
		RefreshToken refreshToken = refreshTokenRepository.findByUser(user).orElse(null);
		if (refreshToken == null) {
			refreshToken = RefreshToken.builder().user(user).token(UUID.randomUUID().toString())
					.expiryDate(Instant.now().plusSeconds(5 * 24 * 60 * 60)).build();
		} else {
			refreshToken.setToken(UUID.randomUUID().toString());
			refreshToken.setExpiryDate(Instant.now().plusSeconds(5 * 24 * 60 * 60));
		}

		RefreshToken savedToken = refreshTokenRepository.save(refreshToken);
		return this.modelMapper.map(savedToken, RefreshTokenDto.class);
	}

	@Override
	public RefreshTokenDto findByToken(String token) {
		RefreshToken refreshToken = refreshTokenRepository.findByToken(token)
				.orElseThrow(() -> new ResourceNotFoundException("Refresh Token not found with given token!!"));
		return this.modelMapper.map(refreshToken, RefreshTokenDto.class);
	}

	@Override
	public RefreshTokenDto verifyRefreshToken(RefreshTokenDto token) {
		RefreshToken refreshToken = modelMapper.map(token, RefreshToken.class);
		if (token.getExpiryDate().compareTo(Instant.now()) < 0) {
			refreshTokenRepository.delete(refreshToken);
			throw new RuntimeException("Refresh Token Expired!!");
		}

		return token;

	}

	@Override
	public UserDto getUser(RefreshTokenDto dto) {
		RefreshToken refreshToken = refreshTokenRepository.findByToken(dto.getToken())
				.orElseThrow(() -> new ResourceNotFoundException("Token not found!!"));
		User user = refreshToken.getUser();
		return this.modelMapper.map(user, UserDto.class);
	}

}
