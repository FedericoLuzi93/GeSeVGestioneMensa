package it.gesev.mensa.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import it.gesev.mensa.entity.Dipendente;

public interface DipendenteRepository extends JpaRepository<Dipendente, Integer>
{
	@Query("select dip from Dipendente dip where dip.ente.idEnte = :idEnte")
	public List<Dipendente> findDipendenteByIdEnte(@Param("idEnte") Integer idEnte);
	
	public Optional<Dipendente> findByCodiceFiscale(String codiceFiscale);
	
}
