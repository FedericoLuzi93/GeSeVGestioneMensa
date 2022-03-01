package it.gesev.mensa.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import it.gesev.mensa.entity.Report;

public interface ReportRepository extends JpaRepository<Report, Integer> 
{
	@Query("select r from Report r where tipoReport.codiceTipoReport = :idTipoReeport")
	public List<Report> getReportByTipo(@Param("idTipoReeport") Integer idTipoReeport);

}
