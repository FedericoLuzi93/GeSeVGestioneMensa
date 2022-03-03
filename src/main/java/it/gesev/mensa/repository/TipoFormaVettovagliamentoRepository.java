package it.gesev.mensa.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import it.gesev.mensa.entity.TipoFormaVettovagliamento;

public interface TipoFormaVettovagliamentoRepository extends JpaRepository<TipoFormaVettovagliamento,Integer>
{
	public Optional<TipoFormaVettovagliamento> findByDescrizione(String descrizioneTipoVettovagliamento);
}
