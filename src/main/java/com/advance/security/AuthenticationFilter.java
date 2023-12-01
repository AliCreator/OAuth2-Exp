package com.advance.security;

import java.io.IOException;
import java.util.List;
import java.util.Set;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class AuthenticationFilter extends OncePerRequestFilter {

	private final TokenProvider provider;

	private static final String JWT = "jwt";

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {

		Cookie[] cookies = request.getCookies();
		String jwt = null;
		for (Cookie cookie : cookies) {
			if (JWT.equals(cookie.getName())) {
				jwt = cookie.getValue();

				break;
			}
		}

		if (jwt != null && !jwt.isEmpty()) {
			Long id = getUser(request, jwt);
			if (provider.isTokenValid(id, jwt)) {
				List<GrantedAuthority> authorities = provider.getAuthorities(jwt);
				Authentication authentication = provider.getAuthentication(id, authorities, request);
				SecurityContextHolder.getContext().setAuthentication(authentication);
			} else {
				SecurityContextHolder.clearContext();
			}
		}
		filterChain.doFilter(request, response);

	}

	private Long getUser(HttpServletRequest request, String jwt) {
		return provider.getSubjet(jwt, request);
	}

	@Override
	protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {

		final Set<String> WHITE_LISTED_URIS = Set.of("/auth/register", "/auth/login","/oauth2/authorization/github", "/products", "/products/find/**",
				"/products/update/**", "/orders/**");

		if (request.getMethod().equalsIgnoreCase("OPTIONS")) {
			return true;
		}

		Cookie[] cookies = request.getCookies();
		boolean jwtCookiePresent = false;
		if (cookies != null) {
			for (Cookie cookie : cookies) {
				if ("jwt".equalsIgnoreCase(cookie.getName())) {
					jwtCookiePresent = true;
					break;
				}
			}
		}
		if (!jwtCookiePresent) {
			return true;
		}

		return WHITE_LISTED_URIS.contains(request.getRequestURI());

	}
}
