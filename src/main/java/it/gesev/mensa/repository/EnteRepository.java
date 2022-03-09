package it.gesev.mensa.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import it.gesev.mensa.entity.Ente;

public interface EnteRepository extends JpaRepository<Ente, Integer> 
{
//	@Query("select e from Ente e where e.idEnte = :idEnte")
//	public Ente getMensaPerEnte(int idEnte);
	
//	public Ente findByIdEnte(int idEnte);
}
