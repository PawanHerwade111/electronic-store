package com.electronic.store.security;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

	private Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

	@Autowired
	private JwtHelper jwtHelper;
	@Autowired
	private UserDetailsService userDetailsService;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		// run before api to validate the token

		// Authorization : Bearer opdxwhidfcbniewfdhwio
		String requestHeader = request.getHeader("Authorization");
		logger.info("Header{}:: ", requestHeader);

		String userName = null;
		String token = null;
		if (requestHeader != null && requestHeader.startsWith("Bearer")) {
			token = requestHeader.substring(7);
			try {
				userName = jwtHelper.getUserNameFromToken(token);
				logger.info("Token userName: {} ", userName);
			} catch (IllegalArgumentException ex) {
				logger.info("Illegal Argument while fetching userName from Token!" + ex.getMessage());
			} catch (ExpiredJwtException ex) {
				logger.info("Given Jwt is expired!!" + ex.getMessage());
			} catch (MalformedJwtException ex) {
				logger.info("Some change has been done in Token!!Invalid Token" + ex.getMessage());
			} catch (Exception ex) {
				ex.printStackTrace();
			}

		} else {
			logger.info("Invalid Header!Header is not starting with Bearer");
		}

		if (userName != null && SecurityContextHolder.getContext().getAuthentication() == null) {
			UserDetails userDetails = userDetailsService.loadUserByUsername(userName);
			if (userName.equals(userDetails.getUsername()) && !jwtHelper.isTokenExpired(token)) {
				UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
						userDetails, null, userDetails.getAuthorities());
				authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
				SecurityContextHolder.getContext().setAuthentication(authentication);
			}
		}

		filterChain.doFilter(request, response);
	}

}
