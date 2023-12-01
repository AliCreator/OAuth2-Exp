package com.advance.security;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import com.advance.entity.User;
import com.advance.enumeration.Role;
import com.advance.exception.ApiException;
import com.advance.repository.UserRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
@Transactional
public class CustomeUserDetailsService implements UserDetailsService {

	private final UserRepository userRepo;

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		User user = userRepo.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("User not found!"));
		return new UserPrincipal(user, user.getRole());
	}

}
