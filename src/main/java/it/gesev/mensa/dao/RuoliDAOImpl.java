package it.gesev.mensa.dao;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import it.gesev.mensa.dto.AssDipendenteRuoloDTO;
import it.gesev.mensa.dto.RicercaColonnaDTO;
import it.gesev.mensa.entity.AssDipendenteRuolo;
import it.gesev.mensa.entity.AttestazioneClient;
import it.gesev.mensa.entity.Dipendente;
import it.gesev.mensa.entity.DipendenteEsterno;
import it.gesev.mensa.entity.Ente;
import it.gesev.mensa.entity.Fornitore;
import it.gesev.mensa.entity.Mensa;
import it.gesev.mensa.entity.OrganoDirettivo;
import it.gesev.mensa.entity.RuoloMensa;
import it.gesev.mensa.enums.ColonneDipendenteEnum;
import it.gesev.mensa.enums.TipoRuoloEnum;
import it.gesev.mensa.exc.GesevException;
import it.gesev.mensa.repository.AssRuoloDipendenteRepository;
import it.gesev.mensa.repository.AttestazioneClientRepository;
import it.gesev.mensa.repository.DipendenteEsternoRepository;
import it.gesev.mensa.repository.DipendenteRepository;
import it.gesev.mensa.repository.EnteRepository;
import it.gesev.mensa.repository.FornitoreRepository;
import it.gesev.mensa.repository.MensaRepository;
import it.gesev.mensa.repository.OrganoDirettivoRepository;
import it.gesev.mensa.repository.RuoloMensaRepository;
import it.gesev.mensa.service.MailService;

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
	
	@Autowired
	private AttestazioneClientRepository attestazioneReository;
	
	@Autowired
	private FornitoreRepository fornitoreRepository;
	
	@Autowired
	private MailService mailService;
	
	@Value("${gesev.data.format}")
	private String dateFormat;
	
	@Autowired
	private MensaRepository mensaRepository;
	
	@Autowired
	private EnteRepository enteRepository;
	
	@Autowired
	private DipendenteEsternoRepository dipendenteEsternoRepository;
	
	@PersistenceContext
	EntityManager entityManager;
	
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
	public List<AssDipendenteRuolo> getListaDipendenteRuolo(Integer idEnte) 
	{
		logger.info("Ricerca di dipendenti/ruoli...");
		List<AssDipendenteRuolo> listaDipendentiRuolo = assRuoloDipendenteRepository.findAssociazioneByIdEnte(idEnte);
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
	public List<RuoloMensa> getRuoliByIdOrdineDirettivo(String tipoRuolo) 
	{
		logger.info("Ricerca dei ruoli...");
		
		List<RuoloMensa> listaRuoli = null;
		
		TipoRuoloEnum ruoloEnum;
		try 
		{
			ruoloEnum = TipoRuoloEnum.valueOf(tipoRuolo.toUpperCase());
		} 
		
		catch (Exception e) 
		{
			throw new GesevException("Il tipo ruolo fornito non e' valido", HttpStatus.BAD_REQUEST);
		}
		
		if(ruoloEnum == null)
			throw new GesevException("Il tipo ruolo fornito non e' valido", HttpStatus.BAD_REQUEST);
		
		switch(ruoloEnum)
		{
			case DIPENDENTE:
				listaRuoli = ruoloMensaRepository.getRuoliPerInterni(Sort.by(Sort.Direction.ASC, "descrizioneRuoloMensa"));
				break;
				
			case ESTERNO:
				listaRuoli = ruoloMensaRepository.getRuoliEsterni(Sort.by(Sort.Direction.ASC, "descrizioneRuoloMensa"));
				break;
			default:
				break;
		
		}
		
		if(listaRuoli == null)
			throw new GesevException("Il tipo ruolo fornito non e' valido", HttpStatus.BAD_REQUEST);
		
		logger.info("Trovati " + listaRuoli.size() + " elementi.");
		
		return listaRuoli;
	}

	@Override
	public void aggiungiRuoloDipendente(Integer idDipendente,  String emailDipendente, Integer idRuolo, Integer idOrganoDirettivo, Integer idMensa, Integer idFornitore) throws ParseException 
	{
		logger.info("Aggiunta nuovo ruolo...");
		
		if(idDipendente == null || idRuolo == null)
			throw new GesevException("I dati forniti non sono validi", HttpStatus.BAD_REQUEST);
		
		logger.info("Controllo dati in ingresso...");
		Optional<Dipendente> optionalDipendente = dipendenteRepository.findById(idDipendente);
		Optional<RuoloMensa> optionalRuolo = ruoloMensaRepository.findById(idRuolo);
		Optional<OrganoDirettivo> optionalOrganoDirettivo = null;
		Optional<Mensa> optionalMensa = null;
		
		if(idOrganoDirettivo != null)
		{
			logger.info("Controllo dell'organo direttivo fornito...");
			optionalOrganoDirettivo = organoDirettivoRepository.findById(idOrganoDirettivo);
			if(!optionalOrganoDirettivo.isPresent())
				throw new GesevException("L'identificativo dell'organo direttivo non e' valido", HttpStatus.BAD_REQUEST);
		}
		
		if(idMensa != null)
		{
			logger.info("Controllo mensa...");
			optionalMensa = mensaRepository.findById(idMensa);
			if(!optionalMensa.isPresent())
				throw new GesevException("L'ideenficativo della mensa non e' valido", HttpStatus.BAD_REQUEST);
			
		}
		
		if(!optionalDipendente.isPresent() || !optionalRuolo.isPresent())
			throw new GesevException("Dipendente o ruolo indicati non sono presenti sulla base dati", HttpStatus.BAD_REQUEST);
		
		if(StringUtils.isNotBlank(emailDipendente))
		{
			logger.info("Aggiornamento email dipendente");
			optionalDipendente.get().setEmail(emailDipendente);
			dipendenteRepository.save(optionalDipendente.get());
		}
		
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
		associazione.setMensa(optionalMensa.get());
		
		if(optionalRuolo.get().getFlagPersonaleEsterno().equalsIgnoreCase("Y"))
		{
			if(idFornitore == null)
				throw new GesevException("ID fornitore non valido", HttpStatus.BAD_REQUEST);
			
			Optional<Fornitore> optFornitore = fornitoreRepository.findById(idFornitore);
			if(!optFornitore.isPresent())
				throw new GesevException("ID fornitore non valido", HttpStatus.BAD_REQUEST);
			
			associazione.setFornitore(optFornitore.get());
		}
		
		assRuoloDipendenteRepository.save(associazione);
		
		/* invio mail per operatore mensa */
//		if(optionalRuolo.get().getDescrizioneRuoloMensa().trim().equalsIgnoreCase("Operatore mensa"))
//		{
//			String optCode = mailService.sendMailOperatoreMensa(optionalDipendente.get().getNome(), optionalDipendente.get().getCognome(), optionalDipendente.get().getEmail());
//			inserisciDatiAttestazione(optCode, optionalMensa.isPresent() ? optionalMensa.get() : null, optionalDipendente.get().getCodiceDipendente(), true);
//		}
		
		logger.info("Fine creazione associazione");
		
	}

	@Override
	public List<Dipendente> ricercaDipendenti(List<RicercaColonnaDTO> listaColonne, Integer idEnte) 
	{
		logger.info("Ricerca dei dipendenti sulla base dei seguenti dati : " + listaColonne.stream().map(col -> col.getValue()).collect(Collectors.toList()));
		
		/* definizione della query di ricerca */
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Dipendente> criteriaQuery = criteriaBuilder.createQuery(Dipendente.class);
		Root<Dipendente> fornitoreRoot = criteriaQuery.from(Dipendente.class);
		
		Join<Dipendente, Ente> enteJoin = fornitoreRoot.join("ente");
		
		/* predicato finale per la ricerca */
		Predicate finalPredicate = null;
		
		for(RicercaColonnaDTO ricerca : listaColonne)
		{
			ColonneDipendenteEnum colonnaEnum = null;
			
			/* controllo colonna */
			try
			{
				colonnaEnum = ColonneDipendenteEnum.valueOf(ricerca.getColonna().toUpperCase());
			}
			
			catch(Exception ex)
			{
				throw new GesevException("I valori forniti per la ricerca non sono validi", HttpStatus.BAD_REQUEST);
			}
			
			/* controllo valore */
			if(StringUtils.isBlank(ricerca.getValue()))
				throw new GesevException("I valori forniti per la ricerca non sono validi", HttpStatus.BAD_REQUEST);
			
			Expression<String> espressioneCf = criteriaBuilder.upper(fornitoreRoot.get(colonnaEnum.getColonna()));
			Predicate nuovoPredicato = criteriaBuilder.like(espressioneCf, ricerca.getValue().toUpperCase() + "%");
			
			finalPredicate = finalPredicate == null ? nuovoPredicato : criteriaBuilder.and(finalPredicate, nuovoPredicato);
		}
		
		/* controllo ente */
		Predicate controlloEnte = criteriaBuilder.equal(enteJoin.get("idEnte"), idEnte);
		finalPredicate = criteriaBuilder.and(finalPredicate, controlloEnte);
		
		
		return entityManager.createQuery(criteriaQuery.where(finalPredicate)).getResultList();
	}

	@Override
	public void updateRuoloDipendente(Integer idRuoloDipendente, Integer idRuolo, Integer idDipendente, Integer idOrganoDirettivo, Integer idMensa) {
		logger.info(String.format("Aggiornamento del ruolo dipendente con ID %d con (ID ruolo, ID dipendente) = (%d, %d)", idRuoloDipendente, idRuolo, idDipendente));
		
		logger.info("Controllo esistenza assoociazione...");
		Optional<AssDipendenteRuolo> associazioneOpt = assRuoloDipendenteRepository.findById(idRuoloDipendente);
		if(!associazioneOpt.isPresent())
			throw new GesevException("Impossibile trovare un'associazione con l'ID specificato", HttpStatus.BAD_REQUEST);
		
		logger.info("Controllo esistenza ruolo...");
		Optional<RuoloMensa> ruolo = ruoloMensaRepository.findById(idRuolo);
		if(!ruolo.isPresent())
			throw new GesevException("Impossibile trovare un ruolo con l'ID specificato", HttpStatus.BAD_REQUEST);
		
		logger.info("Controllo esistenza dipendente...");
		Optional<Dipendente> dipendente = dipendenteRepository.findById(idDipendente);
		if(!dipendente.isPresent())
			throw new GesevException("Impossibile trovare un dipendente con l'ID specificato", HttpStatus.BAD_REQUEST);
		
		Optional<OrganoDirettivo> organoDirettivoOpt = null;
		if(idOrganoDirettivo != null)
		{
			logger.info("Controllo esistenza organo direttivo...");
			organoDirettivoOpt = organoDirettivoRepository.findById(idOrganoDirettivo);
			
			if(!organoDirettivoOpt.isPresent())
				throw new GesevException("Impossibile trovare un organo direttivo con l'ID specificato", HttpStatus.BAD_REQUEST);
		}
		
		logger.info("Coontrollo mensa...");
		Optional<Mensa> optionalMensa = null;
		if(idMensa != null)
		{
			logger.info("Controllo mensa...");
			optionalMensa = mensaRepository.findById(idMensa);
			if(!optionalMensa.isPresent())
				throw new GesevException("L'ideenficativo della mensa non e' valido", HttpStatus.BAD_REQUEST);
			
		}
		
		logger.info("Aggiornamento in corso...");
		AssDipendenteRuolo associazione = associazioneOpt.get();
		associazione.setRuolo(ruolo.get());
		associazione.setDipendente(dipendente.get());
		if(organoDirettivoOpt != null && organoDirettivoOpt.isPresent())
			associazione.setOrganoDirettivo(organoDirettivoOpt.get());
		
		else
			associazione.setOrganoDirettivo(null);
		
		if(optionalMensa != null && optionalMensa.isPresent())
			associazione.setMensa(optionalMensa.get());
		
		assRuoloDipendenteRepository.save(associazione);
		
		logger.info("Fine aggiornamento");
		
	}

	@Override
	public void cancellaRuolo(Integer idRuoloDipendente) 
	{
		logger.info("Cancellazione ruolo con ID " + idRuoloDipendente);
		
		logger.info("Controllo esistenza ruolo...");
		if(idRuoloDipendente == null)
			throw new GesevException("Dati forniti non validi", HttpStatus.BAD_REQUEST);
		
		Optional<AssDipendenteRuolo> associazione = assRuoloDipendenteRepository.findById(idRuoloDipendente);
		if(!associazione.isPresent())
			throw new GesevException("Impossibile trovare un ruolo dipendente con l'ID specificato", HttpStatus.BAD_REQUEST);
		
		assRuoloDipendenteRepository.delete(associazione.get());
		
		logger.info("Fine cancellazione ruolo");
			
		
	}

	/* Crea Organo Direttivo */
	@Override
	public int creaOrganoDirettivo(OrganoDirettivo organoDirettivo) 
	{
		logger.info("Creazione organo direttivo in corso...");
		organoDirettivoRepository.save(organoDirettivo);
		logger.info("Creazione organo direttivo avvenuta con successo");
		return organoDirettivo.getCodiceOrganoDirettivo();
	}

	/* Modifica Organo Direttivo */
	@Override
	public int modificaOrganoDirettivo(OrganoDirettivo organoDirettivo, int idOrganoDirettivo) 
	{
		logger.info("Modifica organo direttivo in corso...");
		OrganoDirettivo organoDirettivoMom = null;
		
		//Controllo presenza Organo Direttivo
		Optional<OrganoDirettivo> optionalOrganoDirettivo = organoDirettivoRepository.findById(idOrganoDirettivo);
		if(optionalOrganoDirettivo.isPresent())
		{
			organoDirettivoMom = optionalOrganoDirettivo.get();
			organoDirettivoMom.setDescrizioneOrganoDirettivo(organoDirettivo.getDescrizioneOrganoDirettivo());
			organoDirettivoRepository.save(organoDirettivoMom);
		}
		else
		{
			throw new GesevException("Codice Organo direttivo non presente", HttpStatus.BAD_REQUEST);
		}	
		logger.info("Modifica organo direttivo avvenuta con successo");
		return idOrganoDirettivo;
	}

	/* Cancella Organo Direttivo */
	@Override
	public int cancellaOrganoDirettivo(int idOrganoDirettivo) 
	{
		logger.info("Cancellazione organo direttivo in corso...");
		Optional<OrganoDirettivo> optionalOrganoDirettivo = organoDirettivoRepository.findById(idOrganoDirettivo);
		if(optionalOrganoDirettivo.isPresent())
		{
			organoDirettivoRepository.deleteById(idOrganoDirettivo);
		}
		else
		{
			throw new GesevException("Codice Organo direttivo non presente", HttpStatus.BAD_REQUEST);
		}
		return idOrganoDirettivo;	
	}
	
	@Override
	public List<Dipendente> findDipendenteByIdEnte(Integer idMensa) 
	{
		logger.info("Ricerca dipendenti della mensa con ID " + idMensa);
		
		logger.info("Ricerca mensa...");
		Optional<Mensa> optMensa = mensaRepository.findById(idMensa);
		if(!optMensa.isPresent())
			throw new GesevException("Nessuna mensa presente con l'ID specificato", HttpStatus.BAD_REQUEST);
		
		logger.info("Ricerca ente di apparteneenza della mensa...");
		Mensa mensa = optMensa.get();
		if(mensa.getEnte() == null || mensa.getEnte().getIdEnte() == null)
			throw new GesevException("Impossibile reperire l'ente di appartennza della mensa", HttpStatus.INTERNAL_SERVER_ERROR);
		
		Optional<Ente> optEnte = enteRepository.findById(optMensa.get().getEnte().getIdEnte());
		if(!optEnte.isPresent())
			throw new GesevException("Impossibile reperire l'ente di appartennza della mensa", HttpStatus.INTERNAL_SERVER_ERROR);
		
		return dipendenteRepository.findDipendenteByIdEnte(optEnte.get().getIdEnte());
	}

	@Override
	public void aggiungiRuoloDipendenteEsterno(String nome, String cognome, String email, Integer idRuolo, Integer idMensa, Integer idFornitore) throws ParseException 
	{
		logger.info("Aggiunta ruolo per dipendente esterno...");
		
		logger.info("Controllo dati anagrafici...");
		if(StringUtils.isBlank(email))
			throw new GesevException("L'email e' obbligatoria", HttpStatus.BAD_REQUEST);
		
		if(StringUtils.isBlank(nome))
			throw new GesevException("Il nome e' obbligatorio", HttpStatus.BAD_REQUEST);
		
		if(StringUtils.isBlank(cognome))
			throw new GesevException("Il cognome e' obbligatoria", HttpStatus.BAD_REQUEST);
		
		logger.info("Controllo ruolo...");
		if(idRuolo == null)
			throw new GesevException("Nessun ruolo trovato con l'ID specificato", HttpStatus.BAD_REQUEST);
		
		Optional<RuoloMensa> optRuolo = ruoloMensaRepository.findById(idRuolo);
		if(!optRuolo.isPresent())
			throw new GesevException("Nessun ruolo trovato con l'ID specificato", HttpStatus.BAD_REQUEST);
		
		logger.info("Controllo mensa...");
		if(idMensa == null)
			throw new GesevException("Nessuna mensa trovata con l'ID specificato", HttpStatus.BAD_REQUEST);
		
		Optional<Mensa> optMensa = mensaRepository.findById(idMensa);
		if(!optMensa.isPresent())
			throw new GesevException("Nessuna mensa trovata con l'ID specificato", HttpStatus.BAD_REQUEST);
		
		logger.info("Creazione nuovo dipendente esterno...");
		DipendenteEsterno dipendente = new DipendenteEsterno();
		dipendente.setCognomeDipendenteEsterno(cognome.toUpperCase());
		dipendente.setEmailDipendenteEsterno(email.toUpperCase());
		dipendente.setNomeDipendenteEsterno(nome.toUpperCase());
		
		DipendenteEsterno dipendenteSalvato = dipendenteEsternoRepository.save(dipendente);
		
		logger.info("Creazione nuovo ruolo...");
		AssDipendenteRuolo associazione = new AssDipendenteRuolo();
		SimpleDateFormat formatter = new SimpleDateFormat(this.dateFormat);
		associazione.setDataFineRuolo(formatter.parse("9999-12-31"));
		associazione.setDataInizioRuolo(new Date());
		associazione.setDipendenteEsterno(dipendenteSalvato);
		associazione.setMensa(optMensa.get());
		associazione.setRuolo(optRuolo.get());
		
		if(optRuolo.get().getFlagPersonaleEsterno().equalsIgnoreCase("Y"))
		{
			if(idFornitore == null)
				throw new GesevException("ID fornitore non valido", HttpStatus.BAD_REQUEST);
			
			Optional<Fornitore> optFornitore = fornitoreRepository.findById(idFornitore);
			if(!optFornitore.isPresent())
				throw new GesevException("ID fornitore non valido", HttpStatus.BAD_REQUEST);
			
			associazione.setFornitore(optFornitore.get());
		}
		
		assRuoloDipendenteRepository.save(associazione);
		
		/* invio mail per operatore mensa */
//		if(optRuolo.get().getDescrizioneRuoloMensa().trim().equalsIgnoreCase("Operatore mensa"))
//		{
//			String optCode = mailService.sendMailOperatoreMensa(nome, cognome, email);
//			inserisciDatiAttestazione(optCode, optMensa.get(), dipendenteSalvato.getIdDipendenteEsterno(), false);
//		}
		
	}

	@Override
	public List<AssDipendenteRuolo> findRuoliDipendentiEsterni(Integer codiceMensa) {
		logger.info("Ricerca ruoli per dipendenti esterni...");
		
		return assRuoloDipendenteRepository.findRuoliDipendentiEsterni(codiceMensa);
	}

	@Override
	public void updateRuoloDipendenteEsterno(AssDipendenteRuoloDTO associazione) 
	{
		logger.info("Aggiornamento dipendente esterno...");
		
		if(associazione == null || associazione.getAssDipendenteRuoloId() == null)
			throw new GesevException("Informazioni sull'associazione insufficienti", HttpStatus.BAD_REQUEST);
		
		logger.info("Ricerca associazione...");
		Optional<AssDipendenteRuolo> optAssociazione = assRuoloDipendenteRepository.findById(associazione.getAssDipendenteRuoloId());
		if(!optAssociazione.isPresent())
			throw new GesevException("Identificativo associazione non valido", HttpStatus.BAD_REQUEST);
		
		if(associazione.getDipendente() != null)
		{
			optAssociazione.get().getDipendente().setEmail(associazione.getDipendente().getEmail());
			
		}
		
		else if(associazione.getDipendente() == null)
		{
			optAssociazione.get().getDipendenteEsterno().setCognomeDipendenteEsterno(associazione.getCognomeEsterno());
			optAssociazione.get().getDipendenteEsterno().setNomeDipendenteEsterno(associazione.getNomeEsterno());
			optAssociazione.get().getDipendenteEsterno().setEmailDipendenteEsterno(associazione.getEmailEsterno());
		}
		
		assRuoloDipendenteRepository.save(optAssociazione.get());
	
	}

//	@Override
//	public void inserisciDatiAttestazione(String codiceOtp, Mensa mensa, Integer idDipendente, boolean isDipendente) 
//	{
//		logger.info("Inserimento dati attestazione...");
//		AttestazioneClient attestazione = new AttestazioneClient();
//		attestazione.setCodiceOtp(codiceOtp);
//		attestazione.setMensa(mensa);
//		attestazione.setDataUltimaAttivita(new Date());
//		
//		if(isDipendente)
//		{
//			Optional<Dipendente> optDipendente = dipendenteRepository.findById(idDipendente);
//			if(!optDipendente.isPresent())
//				throw new GesevException("Impossibile trovare un dipendente con ID " + idDipendente, HttpStatus.BAD_REQUEST);
//			
//			attestazione.setDipendente(optDipendente.get());
//		}
//		
//		else
//		{
//			Optional<DipendenteEsterno> optDipendente = dipendenteEsternoRepository.findById(idDipendente);
//			if(!optDipendente.isPresent())
//				throw new GesevException("Impossibile trovare un dipendente esterno con ID " + idDipendente, HttpStatus.BAD_REQUEST);
//			
//			attestazione.setDipendenteEsterno(optDipendente.get());
//		}
//		
//		attestazioneReository.save(attestazione);
//		
//	}

	
}
