package it.gesev.mensa.repository;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import it.gesev.mensa.entity.RuoloMensa;

public interface RuoloMensaRepository extends JpaRepository<RuoloMensa, Integer>
{
//	@Query("select r from RuoloMensa r where r.organoDirettivo.codiceOrganoDirettivo = :idOrdineDirettivo")
//	public List<RuoloMensa> getRuoliByIdOrdineDirettivo(@Param("idOrdineDirettivo") Integer idOrdineDirettivo);
	
	public List<RuoloMensa> findAllByOrderByDescrizioneRuoloMensaAsc();
	
	@Query("select r from RuoloMensa r where r.flagPersonaleEsterno = 'N' and r.flagPersonaleInterno = 'Y'")
	public List<RuoloMensa> getRuoliPerInterni(Sort sort);
	
	@Query("select r from RuoloMensa r where r.flagPersonaleEsterno = 'Y'")
	public List<RuoloMensa> getRuoliEsterni(Sort sort);
}
