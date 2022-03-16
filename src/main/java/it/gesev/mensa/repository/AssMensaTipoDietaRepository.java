package it.gesev.mensa.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import it.gesev.mensa.entity.AssMensaTipoDieta; 

public interface AssMensaTipoDietaRepository extends JpaRepository<AssMensaTipoDieta, Integer> 
{
	@Query("select a from AssMensaTipoDieta a where a.mensa.codiceMensa = :idMensa")
	public List<AssMensaTipoDieta> cercaPerMensa(int idMensa);
	
	@Modifying
	@Query("delete from AssMensaTipoDieta a where a.mensa.codiceMensa = :idMensa")
	public int cancellaPerMensaFK(int idMensa);
}
