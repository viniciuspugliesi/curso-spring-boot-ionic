package com.viniciuspugliesi.cursomc.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import com.viniciuspugliesi.cursomc.domain.Categoria;
import com.viniciuspugliesi.cursomc.repositories.CategoriaRepository;
import com.viniciuspugliesi.cursomc.services.exceptions.DataIntegrityException;
import com.viniciuspugliesi.cursomc.services.exceptions.ObjectNotFountException;

@Service
public class CategoriaService {
	
	@Autowired
	private CategoriaRepository repository;
	
	public List<Categoria> findAll() {
		return repository.findAll();
	}
	
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
	
	public void delete(Integer id) {
		this.find(id);
		
		try {
			repository.delete(id);	
		} catch (DataIntegrityViolationException e) {
			throw new DataIntegrityException("Não é possível excluir uma categoria que possui produtos.");
		}
	}
}
