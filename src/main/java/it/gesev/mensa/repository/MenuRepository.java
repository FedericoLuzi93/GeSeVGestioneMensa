package it.gesev.mensa.repository;

import java.util.Date;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import it.gesev.mensa.entity.Menu;

public interface MenuRepository extends JpaRepository<Menu, Integer> 
{
	@Query("select m from Menu m where m.dataMenu = :dataMenu and m.mensa.codiceMensa = :codiceMensa and m.tipoDieta.idTipoDieta = :tipoDieta")
	public Optional<Menu> cercaMenuDelGiorno(@Param("dataMenu") Date dataMenu, @Param("codiceMensa") Integer codiceMensa, @Param("tipoDieta") Integer tipoDieta);
	

}
