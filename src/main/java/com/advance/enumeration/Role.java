package com.advance.enumeration;

public enum Role {

	ROLE_USER("READ"), ROLE_OWNER("READ,WRITE"), ROLE_ADMIN("READ,WRITE,UPDATE,DELETE"); 
	
	private String permission; 
	
	Role(String permission) {
		this.permission = permission; 
	}
	
	public String getPermission() {
		return this.permission;
	}
}
