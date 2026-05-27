package com.electronic.store.controllers;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.electronic.store.dtos.JwtRequest;
import com.electronic.store.dtos.JwtResponse;
import com.electronic.store.dtos.UserDto;
import com.electronic.store.entities.User;
import com.electronic.store.security.JwtHelper;

@RestController
@RequestMapping("/authentication")
public class AuthenticationController {

	private Logger logger = LoggerFactory.getLogger(AuthenticationController.class);

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private JwtHelper jwtHelper;

	@Autowired
	private UserDetailsService userDetailsService;

	@Autowired
	private ModelMapper modelMapper;

	// method to generate token
	@PostMapping("/generate-token")
	public ResponseEntity<JwtResponse> login(@RequestBody JwtRequest request) {
		logger.info("UserName{}, Password{}", request.getEmail(), request.getPassword());
		this.doAuthenticate(request.getEmail(), request.getPassword());

		User user = (User) userDetailsService.loadUserByUsername(request.getEmail());
		// generate token and send
		String token = jwtHelper.generateToken(user);
		JwtResponse jwtResponse = JwtResponse.builder().token(token).userDto(modelMapper.map(user, UserDto.class))
				.build();
		return ResponseEntity.ok(jwtResponse);

	}

	private void doAuthenticate(String email, String password) {
		try {
			Authentication authentication = new UsernamePasswordAuthenticationToken(email, password);
			authenticationManager.authenticate(authentication);
		} catch (BadCredentialsException ex) {
			throw new BadCredentialsException("Invalid UserName and Password!");
		}
	}

}
