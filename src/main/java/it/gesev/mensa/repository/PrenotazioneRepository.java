package it.gesev.mensa.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import it.gesev.mensa.entity.Prenotazione;

public interface PrenotazioneRepository extends JpaRepository<Prenotazione, Integer> {

	@Query("select p from Prenotazione p where p.mensa.ente.idEnte = :idEnte and p.dataPrenotazione = :dataPrenotazione and "
			+ "p.tipoPasto.codiceTipoPasto = :codiceTipoPasto and p.identificativoSistema.idSistema = :idSistema")
	List<Prenotazione> getListaFiltrata(Integer idEnte, Date dataPrenotazione, Integer codiceTipoPasto, String idSistema);

}
