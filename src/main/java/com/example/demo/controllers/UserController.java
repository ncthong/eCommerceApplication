package com.example.demo.controllers;

import com.example.demo.model.persistence.ErrorResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.CreateUserRequest;

@RestController
@RequestMapping("/api/user")
public class UserController {
	public static final Logger log = LoggerFactory.getLogger(UserController.class);
	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private CartRepository cartRepository;

	@GetMapping("/id/{id}")
	public ResponseEntity<User> findById(@PathVariable Long id) {
		return ResponseEntity.of(userRepository.findById(id));
	}
	
	@GetMapping("/{username}")
	public ResponseEntity<User> findByUserName(@PathVariable String username) {
		User user = userRepository.findByUsername(username);
		return user == null ? ResponseEntity.notFound().build() : ResponseEntity.ok(user);
	}

	@PostMapping("/create")
	public ResponseEntity<?> createUser(@RequestBody CreateUserRequest createUserRequest) {
		if (createUserRequest.getPassword().length() < 7) {
			String errorMessage = "password length must be greater than 7.";
			log.error("[UserController][createUser]: {}", errorMessage);
			ErrorResponse errorResponse = new ErrorResponse(errorMessage);
			return ResponseEntity
					.badRequest()
					.body(errorResponse);
		}
		if (!createUserRequest.getPassword().equals(createUserRequest.getRepeatPassword())) {
			String errorMessage = "Wrong confirm password!!!";
			log.error("[UserController][createUser]: {}", errorMessage);
			ErrorResponse errorResponse = new ErrorResponse(errorMessage);
			return ResponseEntity
					.badRequest()
					.body(errorResponse);
		}
		User user = new User();
		user.setUsername(createUserRequest.getUsername());
		Cart cart = new Cart();
		cartRepository.save(cart);
		user.setCart(cart);
		user.setPassword(bCryptPasswordEncoder.encode(createUserRequest.getPassword()));
		userRepository.save(user);
		log.info("[UserController][createUser]: Create user successfully with name:  {}", createUserRequest.getUsername());
		return ResponseEntity.status(HttpStatus.CREATED).body(user);
	}
	
}
