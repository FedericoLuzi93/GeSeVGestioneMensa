package it.gesev.mensa.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import it.gesev.mensa.entity.AssReportRuoloMensa;

public interface AssReportRuoloMensaRepository extends JpaRepository<AssReportRuoloMensa, Integer> 
{
	@Query("select count(a) from AssReportRuoloMensa a where a.report.codiceReport = :codiceReport")
	public int getNumeroFirmeReport(@Param("codiceReport") String codiceReport);
	
	@Query("select a from AssReportRuoloMensa a where a.report.codiceReport = :codiceReport")
	public List<AssReportRuoloMensa> getAssociazioniByIdReport(@Param("codiceReport") String codiceReport);
	
	@Transactional
	@Modifying
	@Query("delete from AssReportRuoloMensa a where a.report.codiceReport = :codiceReport")
	public int deleteByIdReport(@Param("codiceReport") String codiceReport);
}
