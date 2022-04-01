package it.gesev.mensa.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import it.gesev.mensa.entity.TipoPagamento;

public interface TipoPagamentoRepository extends JpaRepository<TipoPagamento, Integer>  
{

	Optional<TipoPagamento> findByIdTipoPagamento(String tipoPagamento);

}
