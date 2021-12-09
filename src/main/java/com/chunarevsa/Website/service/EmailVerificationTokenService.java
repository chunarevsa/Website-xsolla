package com.chunarevsa.Website.service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import com.chunarevsa.Website.entity.User;
import com.chunarevsa.Website.entity.token.EmailVerificationToken;
import com.chunarevsa.Website.entity.token.TokenStatus;
import com.chunarevsa.Website.repo.EmailVerificationTokenRepository;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class EmailVerificationTokenService {
	
	private static final Logger logger = LogManager.getLogger(EmailVerificationTokenService.class);

	private final EmailVerificationTokenRepository emailVerificationTokenRepository;
	@Value("${jwt.token.expired}")
	private Long tokenExpired;

	@Autowired
	public EmailVerificationTokenService(EmailVerificationTokenRepository emailVerificationTokenRepository) {
		this.emailVerificationTokenRepository = emailVerificationTokenRepository;
	}

	public void createVirficationToken(User user, String token) {
		EmailVerificationToken emailVerificationToken = new EmailVerificationToken();
		emailVerificationToken.setToken(token);
		emailVerificationToken.setTokenStatus(TokenStatus.STATUS_PENDING);
		emailVerificationToken.setUser(user);
		emailVerificationToken.setExpiryDate(Instant.now().plusMillis(tokenExpired));
		logger.info("Generated Email verification token :" + emailVerificationToken );
		emailVerificationTokenRepository.save(emailVerificationToken);
	}

	public void verifyExpiration(EmailVerificationToken verificationToken) {
		
		if (verificationToken.getExpiryDate().compareTo(Instant.now()) < 0) {
			/* throw new InvalidTokenRequestException("Email Verification Token", token.getToken(),
					"Expired token. Please issue a new request"); */
			logger.info("Токен просрочен");
			// TODO: искл 
		}
	}

	public String createNewToken() {
		return UUID.randomUUID().toString();
	}

	public Optional<EmailVerificationToken> findByToken(String token) {
		return emailVerificationTokenRepository.findByToken(token);
	}

	public EmailVerificationToken save (EmailVerificationToken verificationToken) {
		return emailVerificationTokenRepository.save(verificationToken);
	}
	
}
