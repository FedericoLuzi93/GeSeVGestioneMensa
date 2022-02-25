package it.gesev.mensa.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import it.gesev.mensa.entity.RuoloMensa;

public interface RuoloMensaRepository extends JpaRepository<RuoloMensa, Integer>
{
//	@Query("select r from RuoloMensa r where r.organoDirettivo.codiceOrganoDirettivo = :idOrdineDirettivo")
//	public List<RuoloMensa> getRuoliByIdOrdineDirettivo(@Param("idOrdineDirettivo") Integer idOrdineDirettivo);
}
