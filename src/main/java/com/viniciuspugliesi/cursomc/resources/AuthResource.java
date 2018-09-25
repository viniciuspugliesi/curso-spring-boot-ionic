package com.viniciuspugliesi.cursomc.resources;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.viniciuspugliesi.cursomc.dto.EmailDTO;
import com.viniciuspugliesi.cursomc.security.JWTUtil;
import com.viniciuspugliesi.cursomc.security.UserSecurity;
import com.viniciuspugliesi.cursomc.services.AuthService;
import com.viniciuspugliesi.cursomc.services.UserService;

@RestController
@RequestMapping(value="/auth")
public class AuthResource {

	@Autowired
	private JWTUtil jwtUtil;
	
	@Autowired
	private AuthService serivce;
	
	@RequestMapping(value="/refresh-token",method=RequestMethod.POST)
	public ResponseEntity<Void> refreshToken(HttpServletResponse response) {
		UserSecurity user = UserService.authenticated();
		String token = jwtUtil.generateToken(user.getUsername());
		response.addHeader("Authorization", "Bearer " + token);
		return ResponseEntity.noContent().build();
	}
	
	@RequestMapping(value="/forgot",method=RequestMethod.POST)
	public ResponseEntity<Void> forgot(@Valid @RequestBody EmailDTO emailDTO) {
		serivce.sendNewPassword(emailDTO.getEmail());
		return ResponseEntity.noContent().build();
	}
}
