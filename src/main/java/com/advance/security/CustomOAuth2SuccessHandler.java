package com.advance.security;

import java.io.IOException;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class CustomOAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

	private final TokenProvider provider;

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException, ServletException {

		// Redirect to frontend only if it's the initial OAuth2 login

		UserPrincipal user = (UserPrincipal) authentication.getPrincipal();

		String accessToken = provider.generateAccessToken(user);
		String refreshToken = provider.createRefreshToken(user);

		// Set tokens in the response

		setCookie(response, "access_token", accessToken, 1800); // 1800 seconds = 30 minutes
		setCookie(response, "refresh_token", refreshToken, 604800); // 604800 seconds = 7 days

		request.getSession().setAttribute("OAUTH2_AUTHENTICATED", true);
		getRedirectStrategy().sendRedirect(request, response, "http://localhost:3000/oauth2/redirect");

	}

	private void setCookie(HttpServletResponse response, String name, String value, int maxAge) {
		Cookie cookie = new Cookie(name, value);
		cookie.setPath("/");
		cookie.setHttpOnly(true);
		cookie.setMaxAge(maxAge);
		response.addCookie(cookie);
	}
}
