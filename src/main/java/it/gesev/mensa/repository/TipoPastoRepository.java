package it.gesev.mensa.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import it.gesev.mensa.entity.ServizioEvento;
import it.gesev.mensa.entity.TipoPasto;

public interface TipoPastoRepository extends JpaRepository<TipoPasto, Integer> 
{
	@Query("select s from ServizioEvento s where s.mensa.codiceMensa = :idMensa")
	public List<ServizioEvento> cercaPerMensa(int idMensa);

}
