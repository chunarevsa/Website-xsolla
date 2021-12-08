package com.chunarevsa.Website.security.jwt;

import java.io.IOException;
import java.util.List;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.chunarevsa.Website.exception.InvalidTokenRequestException;
import com.chunarevsa.Website.security.JwtUserDetailsService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

public class JwtAuthenticationFilter extends OncePerRequestFilter {

	@Value("${jwt.header}")
	private String tokenRequestHeader;

	@Value("${jwt.header.prefix}")
	private String tokenRequestHeaderPrefix;

	// jwt.header=Authorization
	// jwt.header.prefix=Bearer 

	@Autowired
	private JwtTokenProvider jwtTokenProvider;

	@Autowired
	private JwtTokenValidator jwtTokenValidator;

	@Autowired
	private JwtUserDetailsService jwtUserDetailsService;

	@Override
	protected void doFilterInternal(
							HttpServletRequest request, 
							HttpServletResponse response, 
							FilterChain filterChain) throws ServletException, IOException {
		try {

			String jwt = getJwtFromRequest(request);			
			if (StringUtils.hasText(jwt) && jwtTokenValidator.validateToken(jwt)) {
				
				Long userId = jwtTokenProvider.getUserIdFromJWT(jwt);
				UserDetails userDetails = jwtUserDetailsService.loadUserById(userId);
				List<GrantedAuthority> authorities = jwtTokenProvider.getAuthoritiesFromJWT(jwt);
				UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, jwt, authorities);
				authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
				SecurityContextHolder.getContext().setAuthentication(authentication);
		  }
		} catch (Exception | InvalidTokenRequestException e) {
			// доделать обработка ошибки 
			System.err.println("Пользователь не авторизован");
		}
		filterChain.doFilter(request, response);
	}

	// Получение токена из запроса
	private String getJwtFromRequest(HttpServletRequest request) {
		String token = request.getHeader(tokenRequestHeader); // jwt.header=Authorization		
		if (StringUtils.hasText(token) && token.startsWith(tokenRequestHeaderPrefix)) { // jwt.header.prefix=Bearer 
			return token.replace(tokenRequestHeaderPrefix, ""); // jwt.header.prefix=Bearer 
		}
		return null;
	}

	
	
}
