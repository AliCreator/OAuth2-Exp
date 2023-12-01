package com.advance.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.ListCrudRepository;

import com.advance.entity.User;
import com.advance.enumeration.Role;

public interface UserRepository extends ListCrudRepository<User, Long>, CrudRepository<User, Long>{

	Optional<User> findByEmail(String email);
	
	Optional<User>  findByRole(Role role);
	
	Optional<User> findByProviderAndPrivderId(String provider, String providerId); 
}
