package it.gesev.mensa.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import it.gesev.mensa.entity.RuoloMensa;

public interface RuoloMensaRepository extends JpaRepository<RuoloMensa, Integer>
{
	@Query("select r from RuoloMensa r where r.organoDirettivo.codiceOrganoDirettivo = :idOrdineDirettivo")
	public List<RuoloMensa> getRuoliByIdOrdineDirettivo(@Param("idOrdineDirettivo") Integer idOrdineDirettivo);
}
