package com.viniciuspugliesi.cursomc.services;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.viniciuspugliesi.cursomc.domain.Cliente;
import com.viniciuspugliesi.cursomc.domain.ItemPedido;
import com.viniciuspugliesi.cursomc.domain.PagamentoComBoleto;
import com.viniciuspugliesi.cursomc.domain.Pedido;
import com.viniciuspugliesi.cursomc.domain.enums.EstadoPagamento;
import com.viniciuspugliesi.cursomc.repositories.ItemPedidoRepository;
import com.viniciuspugliesi.cursomc.repositories.PagamentoRepository;
import com.viniciuspugliesi.cursomc.repositories.PedidoRepository;
import com.viniciuspugliesi.cursomc.security.UserSecurity;
import com.viniciuspugliesi.cursomc.services.exceptions.AuthorizationException;
import com.viniciuspugliesi.cursomc.services.exceptions.ObjectNotFountException;

@Service
public class PedidoService {
	
	@Autowired
	private PedidoRepository pedidoRepository;

	@Autowired
	private BoletoService boletoService;
	
	@Autowired
	private PagamentoRepository pagamentoRepository;

	@Autowired
	private ProdutoService produtoService;

	@Autowired
	private ItemPedidoRepository itemPedidoRepository;

	@Autowired
	private ClienteService clienteService;

	@Autowired
	private EmailService emailService;
	
	public Pedido find(Integer id) {
		Pedido pedido = pedidoRepository.findOne(id);
		
		if (pedido == null) {
			throw new ObjectNotFountException("Objecto não encontrado! ID: " + id + ", Tipo: " + Pedido.class.getName());
		}
		
		return pedido;
	}
	
	@Transactional
	public Pedido insert(Pedido pedido) {
		pedido.setId(null);
		pedido.setInstante(new Date());
		pedido.setCliente(clienteService.find(pedido.getCliente().getId()));
		pedido.getPagamento().setEstado(EstadoPagamento.PENDENTE);
		pedido.getPagamento().setPedido(pedido);
		
		if (pedido.getPagamento() instanceof PagamentoComBoleto) {
			PagamentoComBoleto pagto = (PagamentoComBoleto) pedido.getPagamento();
			boletoService.preencherPagamentoComBoleto(pagto, pedido.getInstante());
		}
		
		pedido = pedidoRepository.save(pedido);
		pagamentoRepository.save(pedido.getPagamento());

		for (ItemPedido ip : pedido.getItens()) {
			ip.setDesconto(0.0);
			ip.setProduto(produtoService.find(ip.getProduto().getId()));
			ip.setPreco(ip.getProduto().getPreco());
			ip.setPedido(pedido);
		}
		
		itemPedidoRepository.save(pedido.getItens());
		emailService.sendOrderConfirmationHtmlEmail(pedido);
		
		return pedido;
	}

	public Page<Pedido> findPage(Integer page, Integer linesPerPage, String orderBy, String direction) {
		UserSecurity user = UserService.authenticated();
		if (user == null) {
			throw new AuthorizationException("Acesso negado");
		}
		
		if (page != 0) {
			page -= 1;
		}
		
		PageRequest pageRequest = new PageRequest(page, linesPerPage, Direction.valueOf(direction), orderBy);
		Cliente cliente = clienteService.find(user.getId());
		
		return pedidoRepository.findByCliente(cliente, pageRequest);
	}
}
