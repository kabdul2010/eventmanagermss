package com.govtech.util;

import java.io.IOException;
import java.util.Base64;
import java.util.Collections;
import java.util.Date;
import java.util.Optional;
import java.util.Random;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.govtech.dtos.GooglePayload;
import com.govtech.entity.User;
import com.govtech.service.IUserService;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Header;
import io.jsonwebtoken.Jwt;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class JWTUtil {

	@Autowired
	private IUserService userService;

	@Value("${app.secret.key}")
	private String jwtSecret;

	@Value("${govtech.app.jwtExpirationMs}")
	private int jwtExpirationMs;

	// Generate JWT token
	public String generateJWTToken(String username, Set<String> roles) {
		String tokenId = String.valueOf(new Random().nextInt(10000));

		return Jwts.builder().setId(tokenId).setSubject(username).setIssuer("GovTech").setAudience("GovTech")
				.setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(System.currentTimeMillis() + jwtExpirationMs))
				.signWith(SignatureAlgorithm.HS512, Base64.getEncoder().encode(jwtSecret.getBytes())).compact();
	}

	// Parse JWT token and extract claims
	public static Claims getClaims(String token, String jwtSecret1) {
		try {

			int i = token.lastIndexOf('.');
			String withoutSignature = token.substring(0, i + 1);
			Jwt<Header, Claims> untrusted = Jwts.parser().parseClaimsJwt(withoutSignature);

			Claims claims = untrusted.getBody();

			System.out.println("Claims " + claims);
			return claims;
		} catch (Exception e) {

			// Handle token parsing error
			e.printStackTrace();
			return null;
		}
	}

	// Validate if the token is expired
	public boolean isTokenExpired(String token) {
		Date expirationDate = getExpirationDate(token);
		return expirationDate != null && expirationDate.before(new Date());
	}

	// Get expiration date of the token
	public Date getExpirationDate(String token) {
		Claims claims = getClaims(token, jwtSecret);
		return claims != null ? claims.getExpiration() : null;
	}

	public boolean isValidToken(String token, String username) {
		String tokenUserName = getSubject(token);
		return username != null && username.equals(tokenUserName) && !isTokenExpired(token);
	}

	public String getSubject(String token) {
		String[] split = token.split("\\.");
		String payload = new String(Base64.getUrlDecoder().decode(split[1]));

		ObjectMapper mapper = new ObjectMapper();
		String email = null;
		GooglePayload parser = null;
		try {
			parser = mapper.readValue(payload, GooglePayload.class);
			email = parser.getEmail();

			if (null != email) {// google users

				// Check if a user with the same email already exists in the database
				Optional<User> findByUsername = userService.findByUsername(email);

				if (findByUsername.isEmpty()) {
					User user = new User();
					user.setUsername(email);
					user.setPassword(email);
					user.setRoles(Collections.singleton("ROLE_USER"));
					userService.saveUser(user);
				}

				System.out.println("email::" + email);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		// this is for the normal Login
		if (email == null) {
			email = parser != null ? parser.getSub() : null;
		}
		return email;
	}

	

}
