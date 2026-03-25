package com.electronic.store.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

@Configuration
public class SecurityConfig {

	@Autowired
	private UserDetailsService userDetailsService;

//	@Bean
//	public UserDetailsService userDetailsService() {
//		//users create
//		UserDetails normal = User.builder().username("Ram").password(passwordEncoder().encode("ram")).roles("NORMAL").build();
//		UserDetails admin = User.builder().username("Pawan").password(passwordEncoder().encode("pawan@123")).roles("ADMIN").build();
//		//InMemoryUserDetailsManager is implementation class of UserDetailsService
//		return new InMemoryUserDetailsManager(normal, admin);
//	}

	@Bean
	public DaoAuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider(this.userDetailsService);
		daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
		return daoAuthenticationProvider;
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
}
