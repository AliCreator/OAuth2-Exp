package com.advance.security;

import static org.springframework.security.config.Customizer.withDefaults;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.advance.handler.CustomAccessDeniedHandler;
import com.advance.handler.CustomAuthenticationEndpoint;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

	private final CustomeUserDetailsService userDetailsService;
	private final BCryptPasswordEncoder encoder;
	private final AuthenticationFilter authenticationFilter;
	private final CustomAccessDeniedHandler accessDeniedHandler;
	private final CustomAuthenticationEndpoint customAuthenticationEndpoint;
	private final CustomOAuth2UserService auth2UserService;
	private final CustomOAuth2SuccessHandler auth2SuccessHandler;

	private static final String[] PUBLIC_URLS = { "/auth/register/**", "/auth/login/**", "/oauth2/**", "/auth/getme/**" };

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http.csrf(c -> c.disable()).cors(withDefaults())
				.sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
				
				.authorizeHttpRequests(auth -> auth.requestMatchers(PUBLIC_URLS).permitAll() // Permit all public URLs
						.requestMatchers(HttpMethod.DELETE, "/user/delete/**").hasAuthority("DELETE:USER")
						.requestMatchers(HttpMethod.DELETE, "/customer/delete/**").hasAuthority("DELETE:CUSTOMER")
						.anyRequest().authenticated()) // All other requests must be authenticated
				.exceptionHandling(ex -> ex.accessDeniedHandler(accessDeniedHandler)
						.authenticationEntryPoint(customAuthenticationEndpoint))
				.addFilterBefore(authenticationFilter, UsernamePasswordAuthenticationFilter.class)
				.oauth2Login(oauth2 -> oauth2.userInfoEndpoint(u -> u.userService(auth2UserService))
						.successHandler(auth2SuccessHandler));
				

		return http.build();
	}

	@Bean
	public AuthenticationManager authenticationManager() {
		DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
		authProvider.setUserDetailsService(userDetailsService);
		authProvider.setPasswordEncoder(encoder);
		return new ProviderManager(authProvider);
	}
}
