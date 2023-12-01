package com.advance.dtoMapper;

import org.springframework.beans.BeanUtils;

import com.advance.dto.UserDTO;
import com.advance.entity.User;

public class DTOMapper {

	public static User convertToUser(UserDTO dto) {
		User user = new User();
		BeanUtils.copyProperties(dto, user);
		return user; 
	}
	
	public static UserDTO covertToDTO(User user) {
		UserDTO dto = new UserDTO();
		BeanUtils.copyProperties(user, dto);
		return dto; 
	}
}
