package it.gesev.mensa.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import it.gesev.mensa.entity.CodiceOTP;

public interface CodiceOTPRepository extends JpaRepository<CodiceOTP, String>
{
	
}
