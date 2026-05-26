package com.electronic.store.security;


import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.impl.lang.Function;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtHelper {
	
	//requirement for token
	//1.validity
	public static final long TOKEN_VALIDITY = 5*60*60*1000;//in millisecond
	
	//2.secret key
	public static final String SECRET_KEY = "apwjkwqeopiwreuincfdnoplfednmieuhqpowjosdidhnijbdweibdewindbindbijbnifdnbihbdbfnkishihwdknodhiuwbnasoiwqyoi";
	
	//retrieve username from jwt token
	public String getUserNameFromToken(String token) {
		return getClaimFromToken(token, Claims::getSubject);
	}

	private <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
		final Claims claims = getAllClaimsFromToken(token);
		return claimsResolver.apply(claims);
	}

	//for retrieveing any info from token we will need the secret key
	private Claims getAllClaimsFromToken(String token) {
		//return Jwts.parser().setSigningKey(SECRET_KEY).build().parseClaimsJws(token).getPayload();
		return Jwts.parser().verifyWith(Keys.hmacShaKeyFor(SECRET_KEY.getBytes())).build().parseSignedClaims(token).getPayload();
	}
	
	//check if token has expired
	public Boolean isTokenExpired(String token) {
		final Date expiration = getExpirationDateFromToken(token);
		return expiration.before(new Date());
	}

	//get expiration date from token
	private Date getExpirationDateFromToken(String token) {
		return getClaimFromToken(token,Claims::getExpiration);
	}
	
	//generate token for user
	public String generateToken(UserDetails userDetails) {
		Map<String, Object> claims = new HashMap<>();
		return doGenerateToken(claims, userDetails.getUsername());
	}

	private String doGenerateToken(Map<String, Object> claims, String username) {
//		return Jwts.builder()
//				.setClaims(claims)
//				.setSubject(username)
//				.setIssuedAt(new Date(System.currentTimeMillis()))
//				.setExpiration(new Date(System.currentTimeMillis() + TOKEN_VALIDITY))
//				.signWith(SignatureAlgorithm.HS512, SECRET_KEY).compact();
		
		return Jwts.builder()
				.claims(claims)
				.subject(username)
				.issuedAt(new Date())
				.expiration(new Date(System.currentTimeMillis() + TOKEN_VALIDITY))
				.signWith(Keys.hmacShaKeyFor(SECRET_KEY.getBytes()), Jwts.SIG.HS512).compact();
				
	}
}
