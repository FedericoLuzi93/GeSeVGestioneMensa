package it.gesev.mensa.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import it.gesev.mensa.entity.Mensa;

public interface MensaRepository  extends JpaRepository<Mensa, Integer> 
{
	@Query("select count(m) from Mensa m where m.via =:via and m.cap =:cap and m.citta =:citta and m.numeroCivico =:numeroCivico")
	public Integer cercaPerIndirizzo(String via, String cap, String citta, int numeroCivico);
	
	@Query("select max(codiceMensa) from Mensa")
	public Integer getMaxMensaId();
	
	public Optional<Mensa> findByCodiceMensa(int idMensa);
}
