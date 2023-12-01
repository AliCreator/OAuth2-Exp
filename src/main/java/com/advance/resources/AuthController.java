package com.advance.resources;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.advance.dto.UserDTO;
import com.advance.dtoMapper.DTOMapper;
import com.advance.entity.HttpResponse;
import com.advance.entity.User;
import com.advance.form.LoginForm;
import com.advance.security.TokenProvider;
import com.advance.security.UserPrincipal;
import com.advance.service.UserService;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

	private final UserService userService;
	private final AuthenticationManager authManager;
	private final TokenProvider provider;

	@PostMapping("/register")
	public ResponseEntity<HttpResponse> registerUser(@RequestBody User user) {
		UserDTO dto = userService.register(user);
		return ResponseEntity.ok()
				.body(HttpResponse.builder().timeStamp(LocalDateTime.now().toString()).message("User registered!")
						.status(HttpStatus.CREATED).statusCode(HttpStatus.CREATED.value()).data(Map.of("user", dto))
						.build());
	}

	@PostMapping("/login")
	public ResponseEntity<HttpResponse> loginUser(@RequestBody LoginForm form, HttpServletResponse response) {
		Authentication authenticate = authManager
				.authenticate(new UsernamePasswordAuthenticationToken(form.getEmail(), form.getPassword()));
		UserPrincipal principal = (UserPrincipal) authenticate.getPrincipal();
		String token = provider.generateAccessToken(principal);
		Cookie cookie = new Cookie("access_token", token);
		cookie.setPath("/");
		cookie.setMaxAge(604800);
		cookie.setHttpOnly(true);
		response.addCookie(cookie);
		UserDTO dto = ((UserPrincipal) authenticate.getPrincipal()).getUser();
		return ResponseEntity.ok()
				.body(HttpResponse.builder().timeStamp(LocalDateTime.now().toString())
						.message("Successfully logged in!").status(HttpStatus.OK).statusCode(HttpStatus.OK.value())
						.data(Map.of("user", dto)).build());
	}

	@GetMapping("/profile")
	public ResponseEntity<HttpResponse> getUserProfile(@AuthenticationPrincipal User user) {

		return ResponseEntity.ok()
				.body(HttpResponse.builder().timeStamp(LocalDateTime.now().toString())
						.message("Successfully logged in!").status(HttpStatus.OK).statusCode(HttpStatus.OK.value())
						.data(Map.of("dto", DTOMapper.covertToDTO(user))).build());
	}
}
