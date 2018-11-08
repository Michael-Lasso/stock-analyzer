package com.bugalu.security;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {
	private final Logger logger = LoggerFactory.getLogger(getClass());

	public static final String USER_ROLE = "user";
	private UserRepository repository;
	private PasswordEncoder passwordEncoder;
	private TokenService tokenService;

	@Autowired
	public UserService(UserRepository repository, PasswordEncoder passwordEncoder, TokenService tokenService) {
		this.repository = repository;
		this.passwordEncoder = passwordEncoder;
		this.tokenService = tokenService;
	}

	public Users findByEmail(String email) {
		logger.info("user not found: yet {}", email);
		// Users u =
		List<Role> roles = new ArrayList<>();
		roles.add(new Role("admin@g00glen00b.be", "admin"));
		Users u = new Users("admin@g00glen00b.be", "g00glen00b",
				"$2a$10$Fg.pwGKNEk8TtRq3C86DEeIo6CnUI05umcVQuvRh2DdwEKJUPtsJK", true, roles);

		// repository.findByEmail(email).orElseThrow(UserNotFoundException::new);
		// List<Users> list = repository.findAll();
		// logger.info("users found: {}", list);
		// Users u = repository.findById(1L).orElseThrow(UserNotFoundException::new);
		logger.info("user found: {}", u);
		return u;
	}

	@Transactional
	public String save(String email, String username, String password) {
		if (repository.findByEmail(email).isPresent()) {
			throw new UsernameTakenException("Username is already taken");
		}
		Role role = new Role(email, USER_ROLE);
		Users user = repository.saveAndFlush(
				new Users(email, username, passwordEncoder.encode(password), true, Collections.singletonList(role)));
		return tokenService.encode(user);
	}

	@PostConstruct
	public void register() {

	}
}
