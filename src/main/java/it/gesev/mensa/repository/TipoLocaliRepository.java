package it.gesev.mensa.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import it.gesev.mensa.entity.TipoLocale;

public interface TipoLocaliRepository extends JpaRepository<TipoLocale,Integer> {
	
	public Optional<TipoLocale> findByCodiceTipoLocale(Integer codiceTipoLocale);

}
