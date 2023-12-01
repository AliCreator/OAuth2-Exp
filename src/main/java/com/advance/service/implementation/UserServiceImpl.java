package com.advance.service.implementation;

import java.util.Optional;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.advance.dto.UserDTO;
import com.advance.dtoMapper.DTOMapper;
import com.advance.entity.User;
import com.advance.enumeration.Role;
import com.advance.exception.ApiException;
import com.advance.exception.UserAlreadyExistException;
import com.advance.exception.UserNotFoundException;
import com.advance.repository.UserRepository;
import com.advance.service.UserService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
	
	private final UserRepository userRepo; 
	private final BCryptPasswordEncoder encoder;
	
	@Override
	public UserDTO getUserById(Long id) {
		try {
			Optional<User> findById = userRepo.findById(id);
			if(findById.isPresent())  {
				return DTOMapper.covertToDTO(findById.get());
			} 
			throw new UserNotFoundException("User with this id is not available!");
		} catch (Exception e) {
			throw new ApiException("An error occured while retrieving user with id!");
		}
	}

	@Override
	public UserDTO register(User user) {
		try {
			Optional<User> findByEmail = userRepo.findByEmail(user.getEmail());
			if(findByEmail.isPresent()) {
				throw new UserAlreadyExistException("User already exit. Please login!");
			}
			User newUser = User.builder().email(user.getEmail()).name(user.getName()).password(encoder.encode(user.getPassword())).role(Role.ROLE_USER).isEnabled(false).build();
			return DTOMapper.covertToDTO(userRepo.save(newUser));
		} catch (Exception e) {
			throw new ApiException("An error occured while registering user");
		}
	}

}
