package it.gesev.mensa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import it.gesev.mensa.entity.CodiceOTP;

public interface CodiceOTPRepository extends JpaRepository<CodiceOTP, String>
{
	@Query("select count(c) from CodiceOTP c where c.codice = :codice")
	public int getCodiceExist(@Param("codice") String codice);
	
}
