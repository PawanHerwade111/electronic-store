package com.electronic.store;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.electronic.store.entities.User;
import com.electronic.store.repositories.UserRepository;
import com.electronic.store.security.JwtHelper;

@SpringBootTest
class ElectronicStoreApplicationTests {
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private JwtHelper jwtHelper;

	@Test
	void contextLoads() {
	}
	
	
	
	@Test
	void testToken(){
		System.out.println("Testing jsw tokens...");
		String name = "pawan";
		User user  = userRepository.findByEmail("pawan@gmail.com").get();
		String token = jwtHelper.generateToken(user);
		System.out.println("token is:: "+token);
		System.out.println("gettting username from token:: "+jwtHelper.getUserNameFromToken(token));
		System.out.println("token expired:: "+ jwtHelper.isTokenExpired(token));
		
	}

}
