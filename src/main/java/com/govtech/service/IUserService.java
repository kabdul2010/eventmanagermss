package com.govtech.service;

import java.util.Optional;

import com.govtech.entity.User;

public interface IUserService {

	Integer saveUser(User user);


	public Optional<User> findUserByEmail(String email);

	public User findUserByResetToken(String resetToken);

	Optional<User> findByUsername(String username);


}
