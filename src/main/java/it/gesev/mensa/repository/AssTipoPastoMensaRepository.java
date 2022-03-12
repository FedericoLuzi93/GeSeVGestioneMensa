package it.gesev.mensa.repository;

import java.util.List;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import it.gesev.mensa.entity.AssTipoPastoMensa;

public interface AssTipoPastoMensaRepository  extends JpaRepository<AssTipoPastoMensa,Integer> 
{
	@Query("select a from AssTipoPastoMensa a where a.mensa.codiceMensa = :idMensa")
	public List<AssTipoPastoMensa> cercaPerMensa(int idMensa);
	
	@Modifying
	@Query("delete from AssTipoPastoMensa a where a.mensa.codiceMensa = :idMensa")
	public int cancellaPerMensaFK(int idMensa);
}
