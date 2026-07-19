package com.electronic.store.config;

import java.util.List;

import org.jspecify.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import com.electronic.store.security.JwtAuthenticationEntryPoint;
import com.electronic.store.security.JwtAuthenticationFilter;

import jakarta.servlet.http.HttpServletRequest;

@EnableWebSecurity(debug = true)
@Configuration
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

	@Autowired
	private UserDetailsService userDetailsService;
	
	@Autowired
	private JwtAuthenticationFilter jwtAuthenticationFilter;
	
	@Autowired
	private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
	
	private final String[] PUBLIC_URLS = {
			"/swagger-ui/**",
			"/webjars/**",
			"/swagger-resources/**",
			"/v3/api-docs/**"

	};
	
	//configure security
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) {
		

		//configurations
//		httpSecurity.authorizeHttpRequests(request ->{
//			// /users-->public
//			request.requestMatchers("/users/*").permitAll();
//			// all others authenticated
//			request.anyRequest().authenticated();
//		});
//		//form based login
//		httpSecurity.formLogin(Customizer.withDefaults());
//		//http javascript based login
//		httpSecurity.httpBasic(Customizer.withDefaults());
//		return httpSecurity.build();
		//httpSecurity.cors(httpSecurityCorsConfigurer -> httpSecurityCorsConfigurer.disable());
		
		//cors config
		httpSecurity.cors(httpSecurityCorsConfigurer -> httpSecurityCorsConfigurer
				.configurationSource(new CorsConfigurationSource() {

					@Override
					public @Nullable CorsConfiguration getCorsConfiguration(HttpServletRequest request) {
						CorsConfiguration corsConfiguration = new CorsConfiguration();
						// origins
						// methods
						// corsConfiguration.addAllowedOrigin("http://localhost:4200");//single
						// corsConfiguration.setAllowedOrigins(List.of("http://localhost:4200",
						// "http://localhost:4300"));//multiple
						corsConfiguration.setAllowedOriginPatterns(List.of("*"));// allow all
						corsConfiguration.setAllowedMethods(List.of("*"));
						corsConfiguration.setAllowCredentials(true);
						corsConfiguration.setAllowedHeaders(List.of("*"));
						corsConfiguration.setMaxAge(5000L);
						return corsConfiguration;
					}
				})

		);
		httpSecurity.csrf(httpSecurityCsrfConfigurer -> httpSecurityCsrfConfigurer.disable());
		httpSecurity.authorizeHttpRequests(request -> request.requestMatchers(HttpMethod.DELETE, "/users/**").hasRole("ADMIN")
				.requestMatchers(HttpMethod.PUT, "/users/**").hasAnyRole("ADMIN","NORMAL")
				.requestMatchers(HttpMethod.GET, "/products/**").permitAll()
				.requestMatchers("/products/**").hasRole("ADMIN")
				.requestMatchers(HttpMethod.GET,"/users/**").permitAll()
				.requestMatchers(HttpMethod.POST,"/users/**").permitAll()
				.requestMatchers(HttpMethod.GET, "/categories/**").permitAll()
				.requestMatchers("/categories/**").hasRole("ADMIN")
				.requestMatchers(HttpMethod.POST, "/authentication/generate-token","/authentication/regenerate-token").permitAll()
				.requestMatchers("/authentication/**").authenticated()
				.requestMatchers(PUBLIC_URLS).permitAll()
				.requestMatchers(HttpMethod.GET).permitAll()
				.anyRequest().permitAll()
				
				
				);
		//httpSecurity.httpBasic(Customizer.withDefaults()); //not needed basic foe jwt
		//JWT Configs
		//entry point if any error comes
		httpSecurity.exceptionHandling(ex -> ex.authenticationEntryPoint(jwtAuthenticationEntryPoint));
		
		//session creation policy
		httpSecurity.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
		
		//main for validation
		httpSecurity.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
	
		return httpSecurity.build();
		
	}

//	@Bean
//	public UserDetailsService userDetailsService() 
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
	
	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration builder) throws Exception {
		return builder.getAuthenticationManager();

	}
}
