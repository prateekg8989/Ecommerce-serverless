package com.axis.security.jwt;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.axis.model.User;
import com.axis.repository.UserRepository;

@Component
public class JwtAuthTokenValidator {

	@Autowired
	private JwtProvider tokenProvider;

	@Autowired
	private UserRepository userRepository;

	public User authenticate(String authHeader) {
		try {
			String jwt = getJwt(authHeader);
			if (jwt != null && tokenProvider.validateJwtToken(jwt)) {
				String username = tokenProvider.getUserNameFromJwtToken(jwt);
				return userRepository.findByUsername(username);
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
			return null;
		}
		return null;
	}

	private String getJwt(String authHeader) {
//		String authHeader = request.getHeader("Authorization");

		if (authHeader != null && authHeader.startsWith("Bearer ")) {
			return authHeader.replace("Bearer ", "");
		}

		return null;
	}
}
