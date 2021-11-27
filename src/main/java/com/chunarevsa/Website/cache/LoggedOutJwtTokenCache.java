package com.chunarevsa.Website.cache;

import java.time.Instant;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import com.chunarevsa.Website.event.UserLogoutSuccess;
import com.chunarevsa.Website.security.jwt.JwtTokenProvider;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import net.jodah.expiringmap.ExpiringMap;

@Component
public class LoggedOutJwtTokenCache {
	
	private final ExpiringMap<String, UserLogoutSuccess> tokenEventMap;

	private final JwtTokenProvider jwtTokenProvider;

	@Autowired
	public LoggedOutJwtTokenCache(@Value("${jwt.cache.logoutToken.maxSize}") int maxSize , JwtTokenProvider jwtTokenProvider) {
		this.tokenEventMap = ExpiringMap.builder().variableExpiration()
																.maxSize(maxSize)
																.build();
		this.jwtTokenProvider = jwtTokenProvider;
	}

	public void markLogoutEventForToken(UserLogoutSuccess event) {
		String token = event.getToken();
		if (tokenEventMap.containsKey(token)) {
			String.format("Log out token is already present ", event.getUserEmail());
		} else {
			Date tokenExpiryDate = jwtTokenProvider.getTokenExpiryFromJwt(token);
			long ttlForToken = getTTLForToken(tokenExpiryDate);
			System.out.println("ttlForToken :" +ttlForToken);

			tokenEventMap.put(token, event, ttlForToken, TimeUnit.SECONDS);

		}
	}

	private long getTTLForToken(Date tokenExpiryDate) {
		long secondAtExpity = tokenExpiryDate.toInstant().getEpochSecond();
		System.out.println("secondAtExpity :" + secondAtExpity);
		long secondAtLogout = Instant.now().getEpochSecond();
		System.out.println("econdAtLogout :" + secondAtLogout);
		return Math.max(0, secondAtExpity - secondAtLogout);
	}

	

	
}
