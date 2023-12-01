package com.advance.security;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import com.advance.dto.UserDTO;
import com.advance.dtoMapper.DTOMapper;
import com.advance.entity.User;
import com.advance.enumeration.Role;

import lombok.RequiredArgsConstructor;

import static java.util.Arrays.stream;
import static java.util.stream.Collectors.toList;

@RequiredArgsConstructor
public class UserPrincipal implements UserDetails, OAuth2User {

	private final User user;
	private final Role role;
	private Map<String, Object> attributes;

	public UserPrincipal(User user, Map<String, Object> attributes) {
		this.user = user;
		this.role = user.getRole(); // Assuming 'getRole' method in User class
		this.attributes = attributes;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return stream(role.getPermission().split(",".trim())).map(SimpleGrantedAuthority::new).collect(toList());
	}

	@Override
	public String getPassword() {

		return user.getPassword() != null ? user.getPassword() : "";
	}

	@Override
	public String getUsername() {
		return user.getEmail();
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return user.isEnabled();
	}

	public UserDTO getUser() {
		return DTOMapper.covertToDTO(user);
	}

	@Override
	public Map<String, Object> getAttributes() {
		return attributes != null ? attributes : Collections.emptyMap();
	}

	@Override
	public String getName() {
		String principalName = user.getEmail(); // or another unique field
		return (principalName != null && !principalName.isEmpty()) ? principalName : "defaultPrincipalName";
	}

}
