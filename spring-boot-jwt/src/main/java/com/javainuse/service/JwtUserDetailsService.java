package com.javainuse.service;

import java.util.ArrayList;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.javainuse.config.WebSecurityConfig;

@Service
public class JwtUserDetailsService implements UserDetailsService {

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		PasswordEncoder passwordString;
		WebSecurityConfig securityConfig = new WebSecurityConfig();
		passwordString = securityConfig.passwordEncoder();
		if ("javainuse".equals(username)) {
			return new User("javainuse", "$2a$10$0x3oQf36mmVRyjBZFAQeT.ZGccq7c/KIrczixwkXubnf4ux1aE1WC",
					new ArrayList<>());
		} else {
			throw new UsernameNotFoundException("User not found with username: " + username);
		}
	}
}