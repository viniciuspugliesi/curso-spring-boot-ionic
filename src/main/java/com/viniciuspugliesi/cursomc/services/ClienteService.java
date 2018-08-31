package com.viniciuspugliesi.cursomc.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.viniciuspugliesi.cursomc.domain.Cliente;
import com.viniciuspugliesi.cursomc.dto.ClienteDTO;
import com.viniciuspugliesi.cursomc.repositories.ClienteRepository;
import com.viniciuspugliesi.cursomc.services.exceptions.DataIntegrityException;
import com.viniciuspugliesi.cursomc.services.exceptions.ObjectNotFountException;

@Service
public class ClienteService {
	
	@Autowired
	private ClienteRepository repository;

	public List<Cliente> findAll() {
		return repository.findAll();
	}
	
	public Cliente find(Integer id) {
		Cliente obj = repository.findOne(id);
		
		if (obj == null) {
			throw new ObjectNotFountException("Objecto não encontrado! ID: " + id + ", Tipo: " + Cliente.class.getName());
		}
		
		return obj;
	}

	public Cliente insert(Cliente obj) {
		obj.setId(null);
		return repository.save(obj);
	}
	
	public Cliente update(Integer id, Cliente obj) {
		Cliente newObj = this.find(id);
		updateData(newObj, obj);
		return repository.save(newObj);
	}
	
	public void delete(Integer id) {
		this.find(id);
		
		try {
			repository.delete(id);	
		} catch (DataIntegrityViolationException e) {
			throw new DataIntegrityException("Não é possível excluir um cliente que possui pedidos.");
		}
	}
	
	public Page<Cliente> findPage(Integer page, Integer linesPerPage, String orderBy, String direction) {
		if (page != 0) {
			page -= 1;
		}
		
		PageRequest pageRequest = new PageRequest(page, linesPerPage, Direction.valueOf(direction), orderBy);
		return repository.findAll(pageRequest);
	}
	
	public Cliente fromDTO(ClienteDTO objDto) {
		return new Cliente(objDto.getId(), objDto.getNome(), objDto.getEmail(), null, null);
	}
	
	private void updateData(Cliente newObj, Cliente obj) {
		newObj.setNome(obj.getNome());
		newObj.setEmail(obj.getEmail());
	}
}
