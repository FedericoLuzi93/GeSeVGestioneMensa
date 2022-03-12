package it.gesev.mensa.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import it.gesev.mensa.entity.AssDipendenteRuolo;

public interface AssRuoloDipendenteRepository extends JpaRepository<AssDipendenteRuolo, Integer> 
{
	@Query("select count(adr) from AssDipendenteRuolo adr where adr.dipendente.codiceDipendente = :idDipendente and adr.ruolo.codiceRuoloMensa = :idRuolo")
	public int findAssociazioneByDipendenteAndRuolo(Integer idDipendente, Integer idRuolo);
	
	@Query("select adr from AssDipendenteRuolo adr where adr.dipendente.ente.idEnte = :idEnte")
	public List<AssDipendenteRuolo> findAssociazioneByIdEnte(@Param("idEnte") Integer idEnte);
}
