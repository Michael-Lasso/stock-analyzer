package com.bugalu.security;

import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UsernamePasswordDetailsService implements UserDetailsService {
	private final Logger logger = LoggerFactory.getLogger(getClass());
	private UserService userService;
	private TokenService tokenService;

	@Autowired
	public UsernamePasswordDetailsService(UserService userService, TokenService tokenService) {
		this.userService = userService;
		this.tokenService = tokenService;
	}

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		logger.info("Trying to authenticate {}", email);
		try {
			UserDetails u = getUserDetails(userService.findByEmail(email));
			logger.info("user: {}", u.getPassword());
			return u;
		} catch (UserNotFoundException ex) {
			throw new UsernameNotFoundException("Account for '" + email + "' not found", ex);
		}
	}

	private TokenUserDetails getUserDetails(Users user) {
		return new TokenUserDetails(user.getEmail(), user.getUsername(), user.getPassword(), tokenService.encode(user),
				user.isEnabled(), getAuthorities(user.getRoles()));
	}

	private List<SimpleGrantedAuthority> getAuthorities(List<Role> roles) {
		return roles.stream().map(role -> new SimpleGrantedAuthority(role.getRole())).collect(Collectors.toList());
	}
}
