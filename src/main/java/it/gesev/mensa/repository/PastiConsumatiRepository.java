package it.gesev.mensa.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import it.gesev.mensa.entity.PastiConsumati;

public interface PastiConsumatiRepository extends JpaRepository<PastiConsumati, Integer> 
{
	@Query("select p from PastiConsumati p where p.mensa.ente.idEnte = :idEnte and p.dataPasto = :dataPasto and "
			+ "p.tipoPasto.codiceTipoPasto = :codiceTipoPasto and p.identificativoSistema.idSistema = :idSistema")
	List<PastiConsumati> getListaFiltrata(Integer idEnte, Date dataPasto, Integer codiceTipoPasto, String idSistema);
}
