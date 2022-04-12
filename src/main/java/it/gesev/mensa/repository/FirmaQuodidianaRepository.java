package it.gesev.mensa.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import it.gesev.mensa.entity.FirmaQuodidiana;

public interface FirmaQuodidianaRepository extends JpaRepository<FirmaQuodidiana, Integer>
{
	@Query("select f.dataFirma from FirmaQuodidiana f where f.dataFirma between :dataInizio and :dataFine")
	public List<Date> getDateFirmeMensili(Date dataInizio, Date dataFine); 
	
}
