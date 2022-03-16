package it.gesev.mensa.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import it.gesev.mensa.entity.ServizioEvento;

public interface ServizioEventoRepository extends JpaRepository<ServizioEvento, Integer>
{
	@Query("select s from ServizioEvento s where s.mensa.codiceMensa = :idMensa")
	public List<ServizioEvento> cercaPerMensa(int idMensa);
	
	@Modifying
	@Query("delete from ServizioEvento s where s.mensa.codiceMensa = :idMensa")
	public int cancellaPerMensaFK(int idMensa);

}
