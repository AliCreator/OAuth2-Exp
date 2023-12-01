package com.advance.service;

import com.advance.dto.UserDTO;
import com.advance.entity.User;

public interface UserService {

	UserDTO register(User user);
	UserDTO getUserById(Long id); 
}
