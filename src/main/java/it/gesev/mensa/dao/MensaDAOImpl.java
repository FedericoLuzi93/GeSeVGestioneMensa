package it.gesev.mensa.dao;

import java.util.List;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import it.gesev.mensa.entity.Mensa;
import it.gesev.mensa.exc.GesevException;
import it.gesev.mensa.repository.MensaRepository;

@Repository
@Component
public class MensaDAOImpl implements MensaDAO 
{
	@Autowired
	private MensaRepository mensaRepository;
	
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
	public int createMensa(Mensa mensa) 
	{
		logger.info("Accesso a createMensa, classe MensaDAOImpl");	
		
		logger.info("Inizio controlli in corso...");		
		//Controllo Stringhe e Numeri 
		if(StringUtils.isBlank(mensa.getDescrizioneMensa()) || StringUtils.isBlank(mensa.getServizioFestivo()) || 
				StringUtils.isBlank(mensa.getNumeroAutorizzazioneSanitaria()) || StringUtils.isBlank(mensa.getAutSanitariaRilasciataDa()) ||
				mensa.getOrarioDal() == null || mensa.getOrarioAl() == null || mensa.getDataAutorizzazioneSanitaria() == null || mensa.getOraFinePrenotazione() == null)
		{
			logger.info("Impossibile creare una mensa. Campi inseriti non validi");
			throw new GesevException("Impossibile creare una mensa. Campi inseriti non validi", HttpStatus.BAD_REQUEST);
		}

		//Controllo unicità
		Integer numMense = mensaRepository.cercaPerIndirizzo(mensa.getVia(), mensa.getCap(), mensa.getCitta(),  mensa.getNumeroCivico());
		if(numMense > 0)
			throw new GesevException("E' gia presente una mensa a questo indirizzo", HttpStatus.BAD_REQUEST);
		
		logger.info("Inserimento nuova mensa in corso...");
		mensaRepository.save(mensa);
		logger.info("Inserimento avvenuto con successo");
		return mensa.getCodiceMensa();
	}

	/* Aggiorna una Mensa */
	@Override
	public int updateMensa(Mensa mensa, int idMensa) 
	{
		logger.info("Accesso a updateMensa, classe MensaDAOImpl");	
		
		//Controllo esistenza idMensa
		Integer maxCodice = mensaRepository.getMaxMensaId();
		if(idMensa > maxCodice || idMensa < 0)
		{
			logger.info("Impossibile modificare la mensa, idMensa non presente");
			throw new GesevException("Impossibile modificare la mensa, idMensa non presente", HttpStatus.BAD_REQUEST);
		}
		
		//Controllo campi mensa
		if(StringUtils.isBlank(mensa.getDescrizioneMensa()) || StringUtils.isBlank(mensa.getServizioFestivo()) || 
				StringUtils.isBlank(mensa.getNumeroAutorizzazioneSanitaria()) || StringUtils.isBlank(mensa.getAutSanitariaRilasciataDa()) ||
				mensa.getOrarioDal() == null || mensa.getOrarioAl() == null || mensa.getDataAutorizzazioneSanitaria() == null || mensa.getOraFinePrenotazione() == null)
		{
			logger.info("Impossibile modificare una mensa. Campi inseriti non validi");
			throw new GesevException("Impossibile modificare una mensa. Campi inseriti non validi", HttpStatus.BAD_REQUEST);
		}

		logger.info("Aggiornamento in corso...");
		mensaRepository.save(mensa);	
		logger.info("Aggiornamento avvenuto con successo");
		return mensa.getCodiceMensa();
	}

}
