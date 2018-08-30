package com.viniciuspugliesi.cursomc.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.viniciuspugliesi.cursomc.domain.Categoria;
import com.viniciuspugliesi.cursomc.repositories.CategoriaRepository;
import com.viniciuspugliesi.cursomc.services.exceptions.ObjectNotFountException;

@Service
public class CategoriaService {
	
	@Autowired
	private CategoriaRepository repository;
	
	public Categoria find(Integer id) {
		Categoria obj = repository.findOne(id);
		
		if (obj == null) {
			throw new ObjectNotFountException("Objecto não encontrado! ID: " + id + ", Tipo: " + Categoria.class.getName());
		}
		
		return obj;
	}
	
	public Categoria insert(Categoria obj) {
		obj.setId(null);
		return repository.save(obj);
	}
	
	public Categoria update(Integer id, Categoria obj) {
		this.find(id);
		obj.setId(id);
		return repository.save(obj);
	}
}
