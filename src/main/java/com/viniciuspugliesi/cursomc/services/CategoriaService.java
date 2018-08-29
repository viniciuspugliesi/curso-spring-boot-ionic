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
	
	public Categoria buscar(Integer id) {
		Categoria obj = repository.findOne(id);
		
		if (obj == null) {
			throw new ObjectNotFountException("Objecto não encontrado! ID: " + id + ", Tipo: " + Categoria.class.getName());
		}
		
		return obj;
	}
}
