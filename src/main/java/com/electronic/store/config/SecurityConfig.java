package com.electronic.store.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@EnableWebSecurity(debug = true)
@Configuration
public class SecurityConfig {

	@Autowired
	private UserDetailsService userDetailsService;
	
	//configure security
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) {
		//configurations
		httpSecurity.authorizeHttpRequests(request ->{
			// /users-->public
			request.requestMatchers("/users/*").permitAll();
			// all others authenticated
			request.anyRequest().authenticated();
		});
		//form based login
		httpSecurity.formLogin(Customizer.withDefaults());
		//http javascript based login
		httpSecurity.httpBasic(Customizer.withDefaults());
		return httpSecurity.build();
		
	}

//	@Bean
//	public UserDetailsService userDetailsService() {
//		//users create
//		UserDetails normal = User.builder().username("Ram").password(passwordEncoder().encode("ram")).roles("NORMAL").build();
//		UserDetails admin = User.builder().username("Pawan").password(passwordEncoder().encode("pawan@123")).roles("ADMIN").build();
//		//InMemoryUserDetailsManager is implementation class of UserDetailsService
//		return new InMemoryUserDetailsManager(normal, admin);
//	}
	
//	@Bean
//	public SecurityFilterChain securityFilterChain(HttpSecurity http) {
////		http.formLogin((formLogin) -> formLogin.loginPage("login.html")
////				.loginProcessingUrl("/process-url")
////				.defaultSuccessUrl("/dashboard")
////				.failureUrl("error")).build();
////		return http.build();
//		
//		
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
