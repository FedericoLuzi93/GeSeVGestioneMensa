package it.gesev.mensa.dao;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import it.gesev.mensa.entity.AssMensaTipoLocale;
import it.gesev.mensa.entity.Ente;
import it.gesev.mensa.entity.Mensa;
import it.gesev.mensa.entity.TipoLocale;
import it.gesev.mensa.exc.GesevException;
import it.gesev.mensa.repository.AssMensaTipoLocaleRepository;
import it.gesev.mensa.repository.EnteRepository;
import it.gesev.mensa.repository.MensaRepository;
import it.gesev.mensa.repository.TipoLocaliRepository;

@Repository
@Component
public class MensaDAOImpl implements MensaDAO 
{
	@Autowired
	private MensaRepository mensaRepository;

	@Autowired
	private TipoLocaliRepository tipoLocaliRepository;

	@Autowired
	private EnteRepository enteRepository;

	@Autowired
	private AssMensaTipoLocaleRepository assMensaTipoLocaleRepository;

	private static final Logger logger = LoggerFactory.getLogger(MensaDAOImpl.class);

	/* Leggi Tutte le Mense */
	@Override
	public List<Mensa> getAllMense() 
	{
		logger.info("Accesso a getAllMense, classe MensaDAOImpl");
		return mensaRepository.findAll();
	}

	/* Crea una Mensa */
	@Override
	public int createMensa(Mensa mensa, List<AssMensaTipoLocale> assMensaTipoLocale) 
	{
		logger.info("Accesso a createMensa, classe MensaDAOImpl");	

		logger.info("Inizio controlli in corso...");		
		//Controllo Campi mensa
		if(mensa.getDescrizioneMensa().isBlank() || mensa.getOrarioDal() == null || mensa.getOrarioAl() == null || 
				mensa.getServizioFestivo().isBlank() || mensa.getOraFinePrenotazione() == null)
		{
			logger.info("Impossibile modificare la mensa, campi mensa non validi");
			throw new GesevException("Impossibile modificare la mensa, campi mensa non validi", HttpStatus.BAD_REQUEST);
		}

		//Controllo Campi Contatto
		if(mensa.getVia().isBlank() || mensa.getNumeroCivico() == null || mensa.getCap().isBlank() || 
				mensa.getCitta().isBlank() || mensa.getProvincia().isBlank() || mensa.getTelefono().isBlank())
		{
			logger.info("Impossibile modificare la mensa, campi contatto non validi");
			throw new GesevException("Impossibile modificare la mensa, campi contatto non validi", HttpStatus.BAD_REQUEST);
		}

		//Salvataggio Mensa
		logger.info("Inserimento nuova mensa in corso...");
		Mensa mensaSalvata = mensaRepository.save(mensa);

		//Salvataggio AssMensaTipoLocale
		for(AssMensaTipoLocale assocazione : assMensaTipoLocale)
		{
			//Controllo AssMensaTipoLocale
			if(assocazione.getSuperficie() <= 0 || assocazione.getNumeroLocali() <0 || assocazione.getTipoLocale().getCodiceTipoLocale() <= 0)
			{
				logger.info("Impossibile modificare la mensa, campi associativi non validi");
				throw new GesevException("Impossibile modificare la mensa, campi associativi non validi", HttpStatus.BAD_REQUEST);
			}
			assocazione.setDataInizio(mensa.getDataInizioServizio());
			assocazione.setDataFine(mensa.getDataFineServizio());
			assocazione.setMensa(mensaSalvata);
			assMensaTipoLocaleRepository.save(assocazione);
		}
		logger.info("Inserimento avvenuto con successo");

		return mensa.getCodiceMensa();
	}

	/* Aggiorna una Mensa */
	@Override
	public int updateMensa(Mensa mensa, List<AssMensaTipoLocale> assMensaTipoLocale, int idMensa) 
	{
		logger.info("Accesso a updateMensa, classe MensaDAOImpl");	
		Mensa mensaMom = null;

		//Controllo esistenza idMensa
		Integer maxCodice = mensaRepository.getMaxMensaId();
		if(idMensa > maxCodice || idMensa < 0)
		{
			logger.info("Impossibile modificare la mensa, Mensa non presente");
			throw new GesevException("Impossibile modificare la mensa, Mensa non presente", HttpStatus.BAD_REQUEST);
		}
		Optional<Mensa> optionalMensa = mensaRepository.findByCodiceMensa(idMensa);
		if(!optionalMensa.isPresent())
			throw new GesevException("Impossibile modificare la mensa, Mensa non presente", HttpStatus.BAD_REQUEST);

		//Controllo Campi mensa
		if(mensa.getDescrizioneMensa().isBlank() || mensa.getOrarioDal() == null || mensa.getOrarioAl() == null || 
				mensa.getServizioFestivo().isBlank() || mensa.getOraFinePrenotazione() == null)
		{
			logger.info("Impossibile modificare la mensa, campi mensa non validi");
			throw new GesevException("Impossibile modificare la mensa, campi mensa non validi", HttpStatus.BAD_REQUEST);
		}

		//Controllo Campi Contatto
		if(mensa.getVia().isBlank() || mensa.getNumeroCivico() == null || mensa.getCap().isBlank() || 
				mensa.getCitta().isBlank() || mensa.getProvincia().isBlank() || mensa.getTelefono().isBlank())
		{
			logger.info("Impossibile modificare la mensa, campi contatto non validi");
			throw new GesevException("Impossibile modificare la mensa, campi contatto non validi", HttpStatus.BAD_REQUEST);
		}

		// Aggiornamento 
		logger.info("Aggiornamento in corso...");
		if(optionalMensa.isPresent())
		{
			mensaMom = mensa;
			mensaMom.setCodiceMensa(idMensa);
			mensaRepository.save(mensaMom);

			for(AssMensaTipoLocale assocazione : assMensaTipoLocale)		
			{
				//Controllo AssMensaTipoLocale
				if(assocazione.getDataInizio() == null || assocazione.getDataFine() == null || assocazione.getSuperficie() <= 0 ||
						assocazione.getNumeroLocali() <0 || assocazione.getTipoLocale().getCodiceTipoLocale() <= 0)
				{
					logger.info("Impossibile modificare la mensa, campi associativi non validi");
					throw new GesevException("Impossibile modificare la mensa, campi associativi non validi", HttpStatus.BAD_REQUEST);
				}
				assocazione.setMensa(mensaMom);
				assMensaTipoLocaleRepository.save(assocazione);
			}
			logger.info("Aggiornamento avvenuto con successo");	
		}
		return mensaMom.getCodiceMensa();
	}

	/* Disabilita Mensa */
	@Override
	public int disableMensa(Mensa mensa, int idMensa) 
	{
		logger.info("Accesso a disableMensa, classe MensaDAOImpl");	
		Mensa mensaMom = null;

		//Controllo esistenza idMensa
		Integer maxCodice = mensaRepository.getMaxMensaId();
		if(idMensa > maxCodice || idMensa < 0)
		{
			logger.info("Impossibile disabilitare la mensa, Mensa non presente");
			throw new GesevException("Impossibile disabilitare la mensa, Mensa non presente", HttpStatus.BAD_REQUEST);
		}

		Optional<Mensa> optionalMensa = mensaRepository.findByCodiceMensa(idMensa);
		if(!optionalMensa.isPresent())
			throw new GesevException("Impossibile disabilitare la mensa, Mensa non presente", HttpStatus.BAD_REQUEST);

		//Controllo Data
		Date dateNow = new Date();
		if(mensa.getDataFineServizio().before(mensa.getDataInizioServizio()) || mensa.getDataFineServizio().before(dateNow) || mensa.getDataFineServizio() == null)
			throw new GesevException("Impossibile disabilitare la mensa, Data Fine Servizio non valida", HttpStatus.BAD_REQUEST);

		// Aggiornamento 
		logger.info("Aggiornamento in corso...");
		if(optionalMensa.isPresent())
		{
			mensaMom = mensa;
			mensaMom.setCodiceMensa(idMensa);
			mensaRepository.save(mensaMom);

		}

		return mensaMom.getCodiceMensa();
	}

	/* --------------------------------------------------------------------------------- */

	/* Lista Locali */
	@Override
	public List<TipoLocale> getAllLocali() 
	{
		logger.info("Accesso a getAllLocali, classe MensaDAOImpl");
		return tipoLocaliRepository.findAll();
	}

	/* Lista Enti */
	@Override
	public List<Ente> getAllEnti() 
	{
		logger.info("Accesso a getAllEnti, classe MensaDAOImpl");
		return enteRepository.findAll();
	}

}
