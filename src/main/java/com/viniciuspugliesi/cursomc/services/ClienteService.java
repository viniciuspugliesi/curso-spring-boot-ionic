package com.viniciuspugliesi.cursomc.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.viniciuspugliesi.cursomc.domain.Cidade;
import com.viniciuspugliesi.cursomc.domain.Cliente;
import com.viniciuspugliesi.cursomc.domain.Endereco;
import com.viniciuspugliesi.cursomc.domain.enums.TipoCliente;
import com.viniciuspugliesi.cursomc.dto.ClienteDTO;
import com.viniciuspugliesi.cursomc.dto.ClienteNewDTO;
import com.viniciuspugliesi.cursomc.repositories.ClienteRepository;
import com.viniciuspugliesi.cursomc.repositories.EnderecoRepository;
import com.viniciuspugliesi.cursomc.services.exceptions.DataIntegrityException;
import com.viniciuspugliesi.cursomc.services.exceptions.ObjectNotFountException;

@Service
public class ClienteService {
	
	@Autowired
	private ClienteRepository clienteRepository;

	@Autowired
	private EnderecoRepository enderecoRepository;

	@Autowired
	private BCryptPasswordEncoder bCrypt;
	
	public List<Cliente> findAll() {
		return clienteRepository.findAll();
	}
	
	public Cliente find(Integer id) {
		Cliente obj = clienteRepository.findOne(id);
		
		if (obj == null) {
			throw new ObjectNotFountException("Objecto não encontrado! ID: " + id + ", Tipo: " + Cliente.class.getName());
		}
		
		return obj;
	}

	@Transactional
	public Cliente insert(Cliente obj) {
		obj.setId(null);
		obj = clienteRepository.save(obj);
		enderecoRepository.save(obj.getEnderecos());
		return obj;
	}
	
	public Cliente update(Integer id, Cliente obj) {
		Cliente newObj = this.find(id);
		updateData(newObj, obj);
		return clienteRepository.save(newObj);
	}
	
	public void delete(Integer id) {
		this.find(id);
		
		try {
			clienteRepository.delete(id);	
		} catch (DataIntegrityViolationException e) {
			throw new DataIntegrityException("Não é possível excluir um cliente que possui pedidos.");
		}
	}
	
	public Page<Cliente> findPage(Integer page, Integer linesPerPage, String orderBy, String direction) {
		if (page != 0) {
			page -= 1;
		}
		
		PageRequest pageRequest = new PageRequest(page, linesPerPage, Direction.valueOf(direction), orderBy);
		return clienteRepository.findAll(pageRequest);
	}
	
	public Cliente fromDTO(ClienteDTO objDto) {
		return new Cliente(objDto.getId(), objDto.getNome(), objDto.getEmail(), null, null, null);
	}
	
	public Cliente fromDTO(ClienteNewDTO objDto) {
		Cliente cli = new Cliente(null, objDto.getNome(), objDto.getEmail(), objDto.getCpfOuCnpj(), TipoCliente.toEnum(objDto.getTipo()), bCrypt.encode(objDto.getSenha()));
		Cidade cid = new Cidade(objDto.getCidadeId(), null, null);
		Endereco end = new Endereco(null, objDto.getLogradouro(), objDto.getNumero(), objDto.getComplemento(), objDto.getBairro(), objDto.getCep(), cli, cid);
		cli.getEnderecos().add(end);
		cli.getTelefones().add(objDto.getTelefone1());
		
		if (objDto.getTelefone2() != null) {
			cli.getTelefones().add(objDto.getTelefone2());
		}
		
		if (objDto.getTelefone3() != null) {
			cli.getTelefones().add(objDto.getTelefone3());
		}
		
		return cli;
	}
	
	private void updateData(Cliente newObj, Cliente obj) {
		newObj.setNome(obj.getNome());
		newObj.setEmail(obj.getEmail());
	}
}
