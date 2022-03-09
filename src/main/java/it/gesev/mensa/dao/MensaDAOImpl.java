package it.gesev.mensa.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import it.gesev.mensa.entity.AssMensaTipoLocale;
import it.gesev.mensa.entity.AssTipoPastoMensa;
import it.gesev.mensa.entity.Ente;
import it.gesev.mensa.entity.Mensa;
import it.gesev.mensa.entity.TipoFormaVettovagliamento;
import it.gesev.mensa.entity.TipoLocale;
import it.gesev.mensa.entity.TipoPasto;
import it.gesev.mensa.exc.GesevException;
import it.gesev.mensa.repository.AssMensaTipoLocaleRepository;
import it.gesev.mensa.repository.AssTipoPastoMensaRepository;
import it.gesev.mensa.repository.EnteRepository;
import it.gesev.mensa.repository.MensaRepository;
import it.gesev.mensa.repository.TipoFormaVettovagliamentoRepository;
import it.gesev.mensa.repository.TipoLocaliRepository;
import it.gesev.mensa.repository.TipoPastoRepository;

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
	
	@Autowired
	private TipoFormaVettovagliamentoRepository tipoFormaVettovagliamentoRepository;
	
	@Autowired
	private TipoPastoRepository tipoPastoRepository;
	
	@Autowired
	private AssTipoPastoMensaRepository assTipoPastoMensaRepository;

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
	@Transactional
	public int createMensa(Mensa mensa, List<AssMensaTipoLocale> assMensaTipoLocale, List<AssTipoPastoMensa> assTipoPastoMensa, String descrizioneTipoVettovagliamento) 
	{
		logger.info("Accesso a createMensa, classe MensaDAOImpl");	

 		logger.info("Inizio controlli in corso...");		
		//Controllo Campi mensa
		if(StringUtils.isBlank(mensa.getDescrizioneMensa()) ||	StringUtils.isBlank(mensa.getServizioFestivo()))
		{
			logger.info("Impossibile creare la mensa, campi mensa non validi");
			throw new GesevException("Impossibile creare la mensa, campi mensa non validi", HttpStatus.BAD_REQUEST);
		}

		//Controllo Campi Contatto
		if(StringUtils.isBlank(mensa.getVia()) || mensa.getNumeroCivico() == null || StringUtils.isBlank(mensa.getCap()) || 
				StringUtils.isBlank(mensa.getCitta()) || StringUtils.isBlank(mensa.getProvincia()) || StringUtils.isBlank(mensa.getTelefono()))
		{
			logger.info("Impossibile creare la mensa, campi contatto non validi");
			throw new GesevException("Impossibile creare la mensa, campi  non validi", HttpStatus.BAD_REQUEST);
		}
		
		//Ricerca TipoVettovagliamento
		Optional<TipoFormaVettovagliamento> optionalTipoFormaVett = tipoFormaVettovagliamentoRepository.findByDescrizione(descrizioneTipoVettovagliamento);
		if(!optionalTipoFormaVett.isPresent())
		{
			logger.info("Impossibile creare la mensa, Tipo Vettovagliamento non valido");
			throw new GesevException("Impossibile creare la mensa, Tipo Vettovagliamento non valido", HttpStatus.BAD_REQUEST);
		}
		TipoFormaVettovagliamento tipoFormaVettovagliamento = optionalTipoFormaVett.get();
		mensa.setTipoFormaVettovagliamento(tipoFormaVettovagliamento);

		//Salvataggio Mensa
		logger.info("Inserimento nuova mensa in corso...");
		Mensa mensaSalvata = mensaRepository.save(mensa);

		//Salvataggio AssMensaTipoLocale
		for(AssMensaTipoLocale associazione : assMensaTipoLocale)
		{
			//Controllo AssMensaTipoLocale
			if(associazione.getSuperficie() <= 0 || associazione.getNumeroLocali() <0 || associazione.getTipoLocale().getCodiceTipoLocale() <= 0)
			{
				logger.info("Impossibile modificare la mensa, campi associativi non validi");
				throw new GesevException("Impossibile modificare la mensa, campi associativi non validi", HttpStatus.BAD_REQUEST);
			}
			associazione.setDataInizio(mensa.getDataInizioServizio());
			associazione.setDataFine(mensa.getDataFineServizio());
			associazione.setMensa(mensaSalvata);
			assMensaTipoLocaleRepository.save(associazione);
		}
		
		//Salvataggio AssTipoPastoMensa
		for(AssTipoPastoMensa assTipoPas : assTipoPastoMensa)
		{
			//Controllo AssTipoPastoMensa
			
			assTipoPas.setMensa(mensaSalvata);
			assTipoPastoMensaRepository.save(assTipoPas);
		}

		logger.info("Inserimento avvenuto con successo");

		return mensa.getCodiceMensa();
	}

	/* Aggiorna una Mensa */
	@Override
	@Transactional
	public int updateMensa(Mensa mensa, List<AssMensaTipoLocale> assMensaTipoLocale, int idMensa, String descrizioneTipoVettovagliamento) 
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
		if(StringUtils.isBlank(mensa.getDescrizioneMensa()) || StringUtils.isBlank(mensa.getServizioFestivo()))
		{
			logger.info("Impossibile modificare la mensa, campi mensa non validi");
			throw new GesevException("Impossibile modificare la mensa, campi mensa non validi", HttpStatus.BAD_REQUEST);
		}

		//Controllo Campi Contatto
		if(StringUtils.isBlank(mensa.getVia()) || mensa.getNumeroCivico() == null || StringUtils.isBlank(mensa.getCap()) || 
				StringUtils.isBlank(mensa.getCitta()) || StringUtils.isBlank(mensa.getProvincia()) || StringUtils.isBlank(mensa.getTelefono()))
		{
			logger.info("Impossibile modificare la mensa, campi contatto non validi");
			throw new GesevException("Impossibile modificare la mensa, campi contatto non validi", HttpStatus.BAD_REQUEST);
		}
		
		//Ricerca TipoVettovagliamento
		Optional<TipoFormaVettovagliamento> optionalTipoFormaVett = tipoFormaVettovagliamentoRepository.findByDescrizione(descrizioneTipoVettovagliamento);
		if(!optionalTipoFormaVett.isPresent())
		{
			logger.info("Impossibile creare la mensa, Tipo Vettovagliamento non valido");
			throw new GesevException("Impossibile creare la mensa, Tipo Vettovagliamento non valido", HttpStatus.BAD_REQUEST);
		}
		TipoFormaVettovagliamento tipoFormaVettovagliamento = optionalTipoFormaVett.get();
		
		// Aggiornamento 
		logger.info("Aggiornamento in corso...");
		if(optionalMensa.isPresent())
		{
			mensaMom = mensa;
			mensaMom.setCodiceMensa(idMensa);
			mensaMom.setTipoFormaVettovagliamento(tipoFormaVettovagliamento);
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
		Date dateSet = mensa.getDataFineServizio();

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

		if(optionalMensa.isPresent())
		{
			mensa = optionalMensa.get();
			mensa.setDataFineServizio(dateSet);
			
			//Controllo Data
			Date dateNow = new Date();
			if(mensa.getDataFineServizio().before(mensa.getDataInizioServizio()) || mensa.getDataFineServizio().before(dateNow) || mensa.getDataFineServizio() == null)
				throw new GesevException("Impossibile disabilitare la mensa, Data Fine Servizio non valida", HttpStatus.BAD_REQUEST);
		
			//Controllo Campi mensa
			if(StringUtils.isBlank(mensa.getDescrizioneMensa()) || StringUtils.isBlank(mensa.getServizioFestivo()))
			{
				logger.info("Impossibile modificare la mensa, campi mensa non validi");
				throw new GesevException("Impossibile modificare la mensa, campi mensa non validi", HttpStatus.BAD_REQUEST);
			}
	
			//Controllo Campi Contatto
			if(StringUtils.isBlank(mensa.getVia()) || mensa.getNumeroCivico() == null || StringUtils.isBlank(mensa.getCap()) || 
					StringUtils.isBlank(mensa.getCitta()) || StringUtils.isBlank(mensa.getProvincia()) || StringUtils.isBlank(mensa.getTelefono()))
			{
				logger.info("Impossibile modificare la mensa, campi contatto non validi");
				throw new GesevException("Impossibile modificare la mensa, campi contatto non validi", HttpStatus.BAD_REQUEST);
			}

			// Aggiornamento 
			logger.info("Aggiornamento in corso...");
			mensaRepository.save(mensa);
			}

		return mensa.getCodiceMensa();
	}
	
	/* Get Singola Mensa */
	@Override
	public Mensa getSingolaMensa(int idMensa) 
	{
		logger.info("Accesso a getSingolaMensa classe MensaDAOImpl");
		Optional<Mensa> optionalMensa =  mensaRepository.findByCodiceMensa(idMensa);
		Mensa mensa = optionalMensa.get();
		return mensa;
	}
	
	/* Invio File */
	@Override
	public Mensa getFile(int idMensa) 
	{
		Optional<Mensa> optionalMensa = mensaRepository.findByCodiceMensa(idMensa);
		Mensa mensa = optionalMensa.get();
		return mensa;
	}
	
	/* Cerca Mense per Id Ente */
	@Override
	public List<Mensa> getMensaPerEnte(int idEnte) 
	{
		logger.info("Accesso a getMensaPerEnte classe MensaDAOImpl");
		Optional<Ente> optionalEnte = enteRepository.findById(idEnte);
		
		//Controlli
		
		List<Mensa> listaMensa = new ArrayList<>();
		Ente ente = optionalEnte.get();
		listaMensa = ente.getListaMensa();
		return listaMensa;
		
	}

	/* --------------------------------------------------------------------------------- */

	/* Lista Locali */
	@Override
	public List<TipoLocale> getAllLocali() 
	{
		logger.info("Accesso a getAllLocali, classe MensaDAOImpl");
		return tipoLocaliRepository.findAll();
	}
	
	/* Lista Locali per Mensa */
	@Override
	public List<AssMensaTipoLocale> getAssMensaTipoLocaleByMensa(int idMensa) 
	{
		logger.info("Accesso a getAssMensaTipoLocaleByMensa, classe MensaDAOImpl");
		return assMensaTipoLocaleRepository.cercaPerMensa(idMensa);
	}

	/* Lista Enti */
	@Override
	public List<Ente> getAllEnti() 
	{
		logger.info("Accesso a getAllEnti, classe MensaDAOImpl");
		return enteRepository.findAll();
	}

	/* Lista Tipo Forma Vettovagliamento */
	@Override
	public List<TipoFormaVettovagliamento> getAllTipoFormaVettovagliamento() 
	{
		logger.info("Accesso a getAllTipoFormaVettovagliamento, classe MensaDAOImpl");
		return tipoFormaVettovagliamentoRepository.findAll();
	}

	/* Lista Tipo Pasto */
	@Override
	public List<TipoPasto> getAllTipoPasto() 
	{
		logger.info("Accesso a getAllTipoPasto classe MensaDAOImpl");
		return tipoPastoRepository.findAll();
	}
	
	/* Get Servizi per idMensa */ 
	@Override
	public List<AssTipoPastoMensa> getServiziPerMensa(int idMensa) 
	{
		logger.info("Accesso a getServiziPerMensa classe MensaDAOImpl");
		List<AssTipoPastoMensa> listaAssMensaTipoLocale = assTipoPastoMensaRepository.cercaPerMensa(idMensa);

		return listaAssMensaTipoLocale;
	}
	
	//----------------------------------------------------------------------------

	/* TipoPasto per ID */
	@Override
	public Optional<TipoPasto> getTipoPastoPerId(int idTipoPasto)
	{
		logger.info("Accesso a getTipoPastoPerId classe MensaDAOImpl");
		return tipoPastoRepository.findById(idTipoPasto);
	}

}
