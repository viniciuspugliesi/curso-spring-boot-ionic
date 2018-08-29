package com.viniciuspugliesi.cursomc.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.viniciuspugliesi.cursomc.domain.Pagamento;
import com.viniciuspugliesi.cursomc.domain.Pedido;

@Repository
public interface PagamentoRepository extends JpaRepository<Pagamento, Integer> {
	
}
