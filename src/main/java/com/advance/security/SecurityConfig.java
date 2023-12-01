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
	
	
	private static final String[] PUBLIC_URLS = { "/auth/register/**", "/auth/login/**", "/oauth2/**" };

	
	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http.csrf(c -> c.disable()).cors(withDefaults());
		http.sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
		http.authorizeHttpRequests(auth -> auth.requestMatchers(PUBLIC_URLS).permitAll());
		http.authorizeHttpRequests(
				auth -> auth.requestMatchers(HttpMethod.DELETE, "/user/delete/**").hasAuthority("DELETE:USER"));
		http.authorizeHttpRequests(
				auth -> auth.requestMatchers(HttpMethod.DELETE, "/customer/delete/**").hasAuthority("DELETE:CUSTOMER"));
		http.exceptionHandling(ex -> ex.accessDeniedHandler(accessDeniedHandler)
				.authenticationEntryPoint(customAuthenticationEndpoint));
		http.authorizeHttpRequests(auth -> auth.anyRequest().authenticated());
		http.addFilterBefore(authenticationFilter, UsernamePasswordAuthenticationFilter.class);
		http.oauth2Login(oauth2 -> oauth2.userInfoEndpoint(u -> u.userService(auth2UserService))); 
		http.oauth2Login(oauth -> oauth.successHandler(auth2SuccessHandler)); 
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
