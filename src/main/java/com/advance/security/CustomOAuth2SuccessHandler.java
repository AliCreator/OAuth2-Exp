package com.advance.security;

import java.io.IOException;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class CustomOAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler{

	private final TokenProvider provider;
	
	
	@Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        UserPrincipal user = (UserPrincipal) authentication.getPrincipal();

        String accessToken = provider.generateAccessToken(user);
        String refreshToken = provider.createRefreshToken(user);

        // Set tokens in the response
        response.addHeader("Access-Token", accessToken);
        response.addHeader("Refresh-Token", refreshToken);

        // Redirect to profile endpoint in the frontend
        getRedirectStrategy().sendRedirect(request, response, "http://localhost:3000/profile");
    }
}
