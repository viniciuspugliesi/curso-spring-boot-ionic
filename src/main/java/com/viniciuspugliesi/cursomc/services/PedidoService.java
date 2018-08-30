package com.viniciuspugliesi.cursomc.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.viniciuspugliesi.cursomc.domain.Pedido;
import com.viniciuspugliesi.cursomc.repositories.PedidoRepository;
import com.viniciuspugliesi.cursomc.services.exceptions.ObjectNotFountException;

@Service
public class PedidoService {
	
	@Autowired
	private PedidoRepository repository;
	
	public Pedido buscar(Integer id) {
		Pedido obj = repository.findOne(id);
		
		if (obj == null) {
			throw new ObjectNotFountException("Objecto não encontrado! ID: " + id + ", Tipo: " + Pedido.class.getName());
		}
		
		return obj;
	}
}
