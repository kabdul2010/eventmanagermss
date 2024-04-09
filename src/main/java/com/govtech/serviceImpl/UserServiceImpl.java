package com.govtech.serviceImpl;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.govtech.entity.User;
import com.govtech.repository.UserRepository;
import com.govtech.service.IUserService;

@Service
public class UserServiceImpl implements IUserService, UserDetailsService {

	@Autowired
	private UserRepository userRepo;

	@Autowired
	private BCryptPasswordEncoder bCryptEncoder;

	@Override
	public Integer saveUser(User user) {
		System.out.println("user details are " + user);
		user.setPassword(bCryptEncoder.encode(user.getPassword()));
		return userRepo.save(user).getUserId();
	}

	@Override
	public Optional<User> findByUsername(String username) {
		return userRepo.findByUsername(username);
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

		System.out.println(">>>>>>>gggggg"+username+"<<<<");
		Optional<User> opt = userRepo.findByUsername(username);
		System.out.println("USER IS PRESENT :" + opt.isPresent());
		
	

		org.springframework.security.core.userdetails.User springUser = null;
		if (opt.isEmpty()) {
			throw new UsernameNotFoundException("User with username: " + username + " not found");
		} else {
			User user = opt.get();    //retrieving user from DB
			Set<String> roles = user.getRoles();
			Set<GrantedAuthority> ga = new HashSet<>();
			for (String role : roles) {
				ga.add(new SimpleGrantedAuthority(role));
			}
			springUser = new org.springframework.security.core.userdetails.User(username, user.getPassword(), ga);
		}
		return springUser;
	}

	@Override
	public Optional<User> findUserByEmail(String email) {
		return userRepo.findByEmail(email);
	}

	@Override
	public User findUserByResetToken(String resetToken) {
		return userRepo.findByResetToken(resetToken);
	}

}

