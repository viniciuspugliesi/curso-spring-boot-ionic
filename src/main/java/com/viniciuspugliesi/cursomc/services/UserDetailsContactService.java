package com.viniciuspugliesi.cursomc.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.viniciuspugliesi.cursomc.domain.Cliente;
import com.viniciuspugliesi.cursomc.repositories.ClienteRepository;
import com.viniciuspugliesi.cursomc.security.UserSecurity;

@Service
public class UserDetailsContactService implements UserDetailsService {

	@Autowired
	private ClienteRepository clienteRepository;
	
	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		Cliente cliente = clienteRepository.findByEmail(email);
		
		if (cliente == null) {
			throw new UsernameNotFoundException(email);
		}
		
		return new UserSecurity(cliente);
	}
}
