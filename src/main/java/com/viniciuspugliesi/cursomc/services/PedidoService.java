package com.viniciuspugliesi.cursomc.services;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.viniciuspugliesi.cursomc.domain.ItemPedido;
import com.viniciuspugliesi.cursomc.domain.PagamentoComBoleto;
import com.viniciuspugliesi.cursomc.domain.Pedido;
import com.viniciuspugliesi.cursomc.domain.enums.EstadoPagamento;
import com.viniciuspugliesi.cursomc.repositories.ItemPedidoRepository;
import com.viniciuspugliesi.cursomc.repositories.PagamentoRepository;
import com.viniciuspugliesi.cursomc.repositories.PedidoRepository;
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
		emailService.sendOrderConfirmationEmail(pedido);
		
		return pedido;
	}
}
