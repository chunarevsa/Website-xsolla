package com.chunarevsa.Website.service;

import java.util.Optional;

import com.chunarevsa.Website.Entity.User;
import com.chunarevsa.Website.Entity.payload.RegistrationRequest;
import com.chunarevsa.Website.Entity.token.EmailVerificationToken;
import com.chunarevsa.Website.Exception.AlredyUseException;
import com.chunarevsa.Website.security.jwt.JwtTokenProvider;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthService { // добавить логи - доделать

	private final UserService userService;
	private final JwtTokenProvider jwtTokenProvider;
	private final EmailVerificationTokenService emailVerificationTokenService;

	@Autowired
	public AuthService(
					UserService userService, 
					JwtTokenProvider jwtTokenProvider,
					EmailVerificationTokenService emailVerificationTokenService) {
		this.userService = userService;
		this.jwtTokenProvider = jwtTokenProvider;
		this.emailVerificationTokenService = emailVerificationTokenService;
	}

	public Optional<User> registrationUser (RegistrationRequest registrationRequest) {
		System.out.println("registrationUser");
		String email = registrationRequest.getEmail();
		if (emailAlreadyExists(email)) {
			throw new AlredyUseException("Email");
	  }
	  System.out.println("addNewUser");
	  User newUser = userService.addNewUser(registrationRequest);
	  System.out.println("saveNewUser");
	  User savedNewUser = userService.save(newUser);
	  System.out.println("registrationUser - ok");
	  return Optional.ofNullable(savedNewUser);
	}

	// Закинуть в валидацию - доделать
	private boolean emailAlreadyExists(String userEmail) {
		return userService.existsByEmail(userEmail);
	}

	public Optional<User> confirmEmailRegistration(String token) { // доделать - обработка исключений
		System.out.println("confirmEmailRegistration");
		EmailVerificationToken verificationToken = emailVerificationTokenService.findByToken(token).orElseThrow();

		User registeredUser = verificationToken.getUser();
		if  (registeredUser.getIsEmailVerified()) {
				return Optional.of(registeredUser);
		}

		System.out.println("verification token");
		emailVerificationTokenService.verifyExpiration(verificationToken);
		verificationToken.setConfirmedStatus();
		emailVerificationTokenService.save(verificationToken);
		System.out.println("verification ok");

		System.out.println("User registered ");
		registeredUser.verificationConfirmed();
		userService.save(registeredUser);
		System.out.println("User registered - ok");

		System.out.println("confirmEmailRegistration - ok");
		return Optional.of(registeredUser);
	}

}
