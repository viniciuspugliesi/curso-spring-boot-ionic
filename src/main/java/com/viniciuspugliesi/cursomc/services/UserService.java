package com.viniciuspugliesi.cursomc.services;

import org.springframework.security.core.context.SecurityContextHolder;

import com.viniciuspugliesi.cursomc.security.UserSecurity;

public class UserService {

	public static UserSecurity authenticated;
	
	public static UserSecurity authenticated() {
		try {
			return (UserSecurity) SecurityContextHolder.getContext().getAuthentication().getPrincipal();			
		} catch (Exception e) {
			return null;
		}
	}
}
