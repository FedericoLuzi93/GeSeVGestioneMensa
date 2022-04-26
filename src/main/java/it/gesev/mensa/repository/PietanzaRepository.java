package it.gesev.mensa.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import it.gesev.mensa.entity.Pietanza;

public interface PietanzaRepository extends JpaRepository<Pietanza, Integer> {

	@Query("Select p from Pietanza p where p.menu.mensa.codiceMensa = :idMensa and p.menu.tipoDieta.idTipoDieta = :idTipoDieta and p.tipoPasto.codiceTipoPasto = :idTipoPasto and p.menu.dataMenu = :dataMenu")
	List<Pietanza> findPietanzeByIdMenu(Integer idMensa, Integer idTipoDieta, Integer idTipoPasto, Date dataMenu);

	@Query("Select p from Pietanza p where p.menu.mensa.codiceMensa = :idMensa and p.menu.tipoDieta.idTipoDieta = :idTipoDieta and p.menu.dataMenu = :dataMenu")
	List<Pietanza> findAllPietanzeByIdMenu(Integer idMensa, Integer idTipoDieta, Date dataMenu);
	
}
