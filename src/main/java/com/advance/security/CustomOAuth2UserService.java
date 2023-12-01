package com.advance.security;

import java.util.HashMap;
import java.util.Map;

import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import com.advance.entity.User;
import com.advance.enumeration.Role;
import com.advance.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService{
	
	private final UserRepository userRepo; 
	
	 @Override
	    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
	        OAuth2User oauth2User = super.loadUser(userRequest);

	        
	        // Extract the necessary information from GitHub's OAuth2User response
	        String provider = userRequest.getClientRegistration().getRegistrationId();
	        Integer githubId = oauth2User.getAttribute("id"); // GitHub 'id' is an Integer
	        String providerId = String.valueOf(githubId); // Convert to String

	        String email = oauth2User.getAttribute("email");
	        if (email == null) {
	           
	            email = providerId + "@gmail.com";
	        }
	        // Custom logic to handle OAuth2 user
	        return processOAuth2User(provider, providerId, email);
	    }
	 
	
	
	 
	 
	 private OAuth2User processOAuth2User(String provider, String providerId, String email) {
	        // Find or create a new user based on the OAuth2 information
	        User existingUser = userRepo.findByProviderAndPrivderId(provider, providerId)
	                              .orElseGet(() -> registerNewUser(provider, providerId, email));

	        // Return an instance of your custom UserDetails implementation
	        Map<String, Object> attributes = new HashMap<>();
	        return new UserPrincipal(existingUser, attributes);
	    }

	    private User registerNewUser(String provider, String providerId, String email) {
	        // Create and return a new user entity
	        User newUser = new User();
	        newUser.setProvider(provider);
	        newUser.setPrivderId(providerId);
	        newUser.setEmail(email);
	        newUser.setEnabled(true); // Assuming the user should be enabled by default
	        newUser.setRole(Role.ROLE_USER);

	        return userRepo.save(newUser);
	    }

}
