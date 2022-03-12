package it.gesev.mensa.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import it.gesev.mensa.entity.AssMensaTipoLocale;
import it.gesev.mensa.entity.AssTipoPastoMensa;
import it.gesev.mensa.entity.Mensa;

public interface AssMensaTipoLocaleRepository  extends JpaRepository<AssMensaTipoLocale,Integer> {
	
	@Query("select a from AssMensaTipoLocale a where a.mensa.codiceMensa =:idMensa")
	public List<AssMensaTipoLocale> cercaPerMensa(int idMensa);
	
	public List<AssTipoPastoMensa> findByMensa(Mensa mensa);
	
	@Modifying
	@Query("delete from AssMensaTipoLocale a where a.mensa.codiceMensa = :idMensa")
	public int cancellaPerMensaFK(int idMensa);


}
