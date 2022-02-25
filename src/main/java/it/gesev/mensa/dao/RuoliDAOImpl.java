package it.gesev.mensa.dao;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import it.gesev.mensa.entity.AssDipendenteRuolo;
import it.gesev.mensa.entity.Dipendente;
import it.gesev.mensa.entity.OrganoDirettivo;
import it.gesev.mensa.entity.RuoloMensa;
import it.gesev.mensa.exc.GesevException;
import it.gesev.mensa.repository.AssRuoloDipendenteRepository;
import it.gesev.mensa.repository.DipendenteRepository;
import it.gesev.mensa.repository.OrganoDirettivoRepository;
import it.gesev.mensa.repository.RuoloMensaRepository;

@Component
public class RuoliDAOImpl implements RuoliDAO 
{

	private static Logger logger = LoggerFactory.getLogger(RuoliDAOImpl.class);
	
	@Autowired
	private DipendenteRepository dipendenteRepository;
	
	@Autowired
	private AssRuoloDipendenteRepository assRuoloDipendenteRepository;
	
	@Autowired
	private OrganoDirettivoRepository organoDirettivoRepository;
	
	@Autowired
	private RuoloMensaRepository ruoloMensaRepository;
	
	@Value("${gesev.data.format}")
	private String dateFormat;
	
	@Override
	public List<Dipendente> getListaDipendenti() 
	{
		logger.info("Ricerca di dipendenti...");
		List<Dipendente> listaDipendenti = dipendenteRepository.findAll();
		logger.info("Trovati " + listaDipendenti.size() + " dipendenti.");
		
		return listaDipendenti;
	}

	@Override
	public List<AssDipendenteRuolo> getListaDipendenteRuolo() 
	{
		logger.info("Ricerca di dipendenti/ruoli...");
		List<AssDipendenteRuolo> listaDipendentiRuolo = assRuoloDipendenteRepository.findAll();
		logger.info("Trovati " + listaDipendentiRuolo.size() + " elementi.");
		
		return listaDipendentiRuolo;
		
	}

	@Override
	public List<OrganoDirettivo> getListaOrganiDirettivi() 
	{
		logger.info("Ricerca degli organi direttivi...");
		List<OrganoDirettivo> listaOrganiDirettivi = organoDirettivoRepository.findAll();
		logger.info("Trovati " + listaOrganiDirettivi.size() + " elementi.");
		
		return listaOrganiDirettivi;
	}

	@Override
	public List<RuoloMensa> getRuoliByIdOrdineDirettivo(Integer idOrganoDirettivo) 
	{
		logger.info("Ricerca dei ruoli associati all'organo direttivo con ID " + idOrganoDirettivo + " ...");
		List<RuoloMensa> listaRuoli = ruoloMensaRepository.getRuoliByIdOrdineDirettivo(idOrganoDirettivo);
		logger.info("Trovati " + listaRuoli.size() + " elementi.");
		
		return listaRuoli;
	}

	@Override
	public void aggiungiRuoloDipendente(Integer idDipendente, Integer idRuolo, Integer idOrganoDirettivo) throws ParseException 
	{
		logger.info("Aggiunta nuovo ruolo...");
		
		if(idDipendente == null || idRuolo == null)
			throw new GesevException("I dati forniti non sono validi", HttpStatus.BAD_REQUEST);
		
		logger.info("Controllo dati in ingresso...");
		Optional<Dipendente> optionalDipendente = dipendenteRepository.findById(idDipendente);
		Optional<RuoloMensa> optionalRuolo = ruoloMensaRepository.findById(idRuolo);
		Optional<OrganoDirettivo> optionalOrganoDirettivo = null;
		
		if(idOrganoDirettivo != null)
		{
			logger.info("Controllo dell'organo direttivo fornito...");
			optionalOrganoDirettivo = organoDirettivoRepository.findById(idOrganoDirettivo);
			if(!optionalOrganoDirettivo.isPresent())
				throw new GesevException("L'identificativo dell'organo direttivo non e' valido", HttpStatus.BAD_REQUEST);
		}
		
		if(!optionalDipendente.isPresent() || !optionalRuolo.isPresent())
			throw new GesevException("Dipendente o ruolo indicati non sono presenti sulla base dati", HttpStatus.BAD_REQUEST);
		
		logger.info("Controllo della presenza dell'associazione richiesta...");
		int associazioneCounter = assRuoloDipendenteRepository.findAssociazioneByDipendenteAndRuolo(idDipendente, idRuolo);
		if(associazioneCounter > 0)
			throw new GesevException("L'associazione richiesta e' gia' preseente", HttpStatus.BAD_REQUEST);
		
		logger.info("Creazione dell'associazione...");
		AssDipendenteRuolo associazione = new AssDipendenteRuolo();
		SimpleDateFormat formatter = new SimpleDateFormat(this.dateFormat);
		associazione.setDipendente(optionalDipendente.get());
		associazione.setRuolo(optionalRuolo.get());
		associazione.setOrganoDirettivo(optionalOrganoDirettivo != null && optionalOrganoDirettivo.isPresent() ? optionalOrganoDirettivo.get() : null);
		associazione.setDataInizioRuolo(new Date());
		associazione.setDataFineRuolo(formatter.parse("9999-12-31"));
		
		assRuoloDipendenteRepository.save(associazione);
		
		logger.info("Fine creazione associazione");
		
	}

}
