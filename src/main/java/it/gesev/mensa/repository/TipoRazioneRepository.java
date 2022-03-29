package it.gesev.mensa.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import it.gesev.mensa.entity.TipoRazione;

public interface TipoRazioneRepository extends JpaRepository<TipoRazione, Integer> {
	
	public Optional<TipoRazione> findByIdTipoRazione(String idTipoRazione);

}
