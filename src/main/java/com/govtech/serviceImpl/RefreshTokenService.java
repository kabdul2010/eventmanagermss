package com.govtech.serviceImpl;


import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.govtech.entity.RefreshToken;
import com.govtech.entity.User;
import com.govtech.exception.UserNotFoundException;
import com.govtech.exception.InvalidInputException;
import com.govtech.repository.RefreshTokenRepository;
import com.govtech.repository.UserRepository;

@Service
public class RefreshTokenService {


	@Value("${govtech.app.jwtRefreshExpirationMs}")
	private Long refreshTokenDurationMs;


	@Autowired
	private RefreshTokenRepository refreshTokenRepository;

	@Autowired
	private UserRepository userRepository;

	public RefreshToken createRefreshToken(String userName) {

		Optional<User> optionalUser = userRepository.findByUsername(userName);

		if (optionalUser.isEmpty()) {
			throw new UserNotFoundException("User with username " + userName + " not found.");
		}

		User user = null;
		RefreshToken refreshToken = null;
		if (optionalUser.isPresent()) {
			user = optionalUser.get();
			refreshToken = user.getRefreshToken();
			if (refreshToken == null) {
				refreshToken = RefreshToken.builder().refreshToken(UUID.randomUUID().toString()).expiry(Instant.now().plusMillis(refreshTokenDurationMs)).user(user).build();
			} else {
				refreshToken.setExpiry(Instant.now().plusMillis(refreshTokenDurationMs));
			}
		}
		user.setRefreshToken(refreshToken);
		refreshTokenRepository.save(refreshToken);
		return refreshToken;
	}

	public RefreshToken verifyRefreshToken(String refreshToken) {

		if (refreshToken == null || refreshToken.isEmpty()) {

			throw new InvalidInputException("Invalid arguments: either Refresh token is null or empty.");
		}

		RefreshToken refreshTokenOb = refreshTokenRepository.findByRefreshToken(refreshToken).orElseThrow(() -> new RuntimeException("Given token doesnot exist in db"));
		if (refreshTokenOb.getExpiry().compareTo(Instant.now()) < 0) {
			refreshTokenRepository.delete(refreshTokenOb);
			throw new RuntimeException("refresh token has been expired.");
		} else {
			return refreshTokenOb;
		}
	}
}
