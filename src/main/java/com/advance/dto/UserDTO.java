package com.advance.dto;

import com.advance.enumeration.Role;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;


@Setter
@Getter
@JsonInclude(value = Include.NON_DEFAULT)
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {

	private Long id;
	private String name;
	private String email;
	private Role role;

}
