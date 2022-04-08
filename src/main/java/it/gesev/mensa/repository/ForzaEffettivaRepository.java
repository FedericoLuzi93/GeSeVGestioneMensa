package it.gesev.mensa.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import it.gesev.mensa.entity.ForzaEffettiva;

public interface ForzaEffettivaRepository extends JpaRepository<ForzaEffettiva, Integer> 
{
	@Query("select f from ForzaEffettiva f where f.ente.idEnte = :idEnte and (f.dataRiferimento between :dataRiferimentoInit and :dataRiferimentoEnd)")
	public List<ForzaEffettiva> listaForzaEffettiva (int idEnte, Date dataRiferimentoInit, Date dataRiferimentoEnd);
}
