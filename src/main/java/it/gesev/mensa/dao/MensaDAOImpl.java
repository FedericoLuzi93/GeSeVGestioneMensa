package it.gesev.mensa.dao;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import it.gesev.mensa.dto.ServizioEventoDTO;
import it.gesev.mensa.dto.TipoLocaleDTO;
import it.gesev.mensa.dto.TipoPastoDTO;
import it.gesev.mensa.entity.AssMensaTipoLocale;
import it.gesev.mensa.entity.AssTipoPastoMensa;
import it.gesev.mensa.entity.Ente;
import it.gesev.mensa.entity.Mensa;
import it.gesev.mensa.entity.ServizioEvento;
import it.gesev.mensa.entity.TipoFormaVettovagliamento;
import it.gesev.mensa.entity.TipoLocale;
import it.gesev.mensa.entity.TipoPasto;
import it.gesev.mensa.exc.GesevException;
import it.gesev.mensa.repository.AssMensaTipoLocaleRepository;
import it.gesev.mensa.repository.AssTipoPastoMensaRepository;
import it.gesev.mensa.repository.EnteRepository;
import it.gesev.mensa.repository.MensaRepository;
import it.gesev.mensa.repository.ServizioEventoRepository;
import it.gesev.mensa.repository.TipoFormaVettovagliamentoRepository;
import it.gesev.mensa.repository.TipoLocaliRepository;
import it.gesev.mensa.repository.TipoPastoRepository;
import it.gesev.mensa.utils.ControlloData;

@Repository
@Component
public class MensaDAOImpl implements MensaDAO 
{
	@Value("${gesev.data.format}")
	private String dateFormat;

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

	@Autowired
	private ServizioEventoRepository servizioEventoRepository;

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
	public int createMensa(Mensa mensa, List<TipoLocaleDTO> listaTipoLocaleDTO, List<TipoPastoDTO> listaTipoPastoDTO,
			List<ServizioEventoDTO> listaServizioEventoDTO, int codiceTipoFormaVettovagliamento, int idEnte) throws ParseException	
	{
		logger.info("Accesso a createMensa, classe MensaDAOImpl");	
		logger.info("Inizio controlli in corso...");	
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateFormat);
		//Controlli Ente
		Optional<Ente> optionalEnte = enteRepository.findById(idEnte);
		if(!optionalEnte.isPresent())
		{
			logger.info("Impossibile creare la mensa, Ente non valido");
			throw new GesevException("Impossibile creare la mensa, ente non valido", HttpStatus.BAD_REQUEST);
		}
		mensa.setEnte(optionalEnte.get());

		//Controlli TipoVettovagliamento
		Optional<TipoFormaVettovagliamento> optionalTipoFormaVett = tipoFormaVettovagliamentoRepository.findById(codiceTipoFormaVettovagliamento);
		if(!optionalTipoFormaVett.isPresent())
		{
			logger.info("Impossibile creare la mensa, Tipo Vettovagliamento non valido");
			throw new GesevException("Impossibile creare la mensa, Tipo Vettovagliamento non valido", HttpStatus.BAD_REQUEST);
		}
		mensa.setTipoFormaVettovagliamento(optionalTipoFormaVett.get());

		//Lista Servzi Festivi
		if(listaServizioEventoDTO == null)
		{
			logger.info("Impossibile creare la mensa, non ci sono Servizi Eventi validi");
			throw new GesevException("Impossibile creare la mensa, non ci sono Servizi Eventi validi", HttpStatus.BAD_REQUEST);
		}

		List<ServizioEvento> listaServizioEvento = new ArrayList<>();

		for(ServizioEventoDTO lsDTO : listaServizioEventoDTO)
		{
			ServizioEvento evento = new ServizioEvento();

			//Controllo Campi
			if(StringUtils.isBlank(lsDTO.getDescrizioneServizioEvento()) || StringUtils.isBlank(lsDTO.getDataServizioEvento()))
			{
				logger.info("Impossibile creare la mensa, campi Servizi Evento non validi");
				throw new GesevException("Impossibile creare la mensa, campi Servizi Evento non validi", HttpStatus.BAD_REQUEST);
			}

			//Conversione
			try 
			{
				evento.setDataServizioEvento(simpleDateFormat.parse(lsDTO.getDataServizioEvento()));
				evento.setDescrizioneServizioEvento(lsDTO.getDescrizioneServizioEvento());
			}
			catch(GesevException exc)
			{
				logger.info("Impossibile creare la mensa, conversione campi Servizi Evento fallita" + exc);
				throw new GesevException("Impossibile creare la mensa, conversione campi Servizi Evento fallita" + exc, HttpStatus.BAD_REQUEST);
			}

			listaServizioEvento.add(evento);
		}

		mensa.setListaServizioEvento(listaServizioEvento);
		mensaRepository.save(mensa);
		Mensa mensaMom = mensa;

		//Lista Associativa Tipo Pasto Mensa
		if(listaTipoPastoDTO == null || listaTipoPastoDTO.isEmpty())
		{
			logger.info("Impossibile creare la mensa, non ci sono Locali validi");
			throw new GesevException("Impossibile creare la mensa, non ci sono Locali validi", HttpStatus.BAD_REQUEST);
		}

		List<AssTipoPastoMensa> listaAssTipoPastoMensas = new ArrayList<>();

		for(TipoPastoDTO tpDTO : listaTipoPastoDTO)
		{
//			//Controllo campi
//			if(tpDTO.getDescrizione().equalsIgnoreCase("pranzo"))
//				if(StringUtils.isBlank(tpDTO.getOraFinePrenotazione()) || StringUtils.isBlank(tpDTO.getOrarioAl()) || StringUtils.isBlank(tpDTO.getOrarioDal()))
//				{
//					logger.info("Impossibile creare la mensa, campi Tipo Pasto non validi");
//					throw new GesevException("Impossibile creare la mensa, campi Tipo Pasto non validi", HttpStatus.BAD_REQUEST);
//				}
//
//			if(tpDTO.getDescrizione().equalsIgnoreCase("cena") || tpDTO.getDescrizione().equalsIgnoreCase("colazione"))
//				if(StringUtils.isBlank(tpDTO.getOrarioAl()) || StringUtils.isBlank(tpDTO.getOrarioDal()))
//				{
//					logger.info("Impossibile creare la mensa, campi Tipo Pasto non validi");
//					throw new GesevException("Impossibile creare la mensa, campi Tipo Pasto non validi", HttpStatus.BAD_REQUEST);
//				}

			//Controllo esistenza Tipo Pasto
			Optional<TipoPasto> optionalTipoPasto = tipoPastoRepository.findById(tpDTO.getCodiceTipoPasto());
			if(!optionalTipoPasto.isPresent())
			{
				logger.info("Impossibile creare la mensa, Tipo Pasto non presente");
				throw new GesevException("Impossibile creare la mensa, Tipo Pasto non presente", HttpStatus.BAD_REQUEST);
			}

			//Controllo Orari 
			if(optionalTipoPasto.isPresent())
			{
				if(optionalTipoPasto.get().getDescrizione().equalsIgnoreCase("pranzo"))
					if(tpDTO.getOrarioAl() == null || tpDTO.getOrarioDal() == null || tpDTO.getOraFinePrenotazione() == null )
						throw new GesevException("Impossibile creare la mensa, orario tipo pasto non valido", HttpStatus.BAD_REQUEST);

				if(optionalTipoPasto.get().getDescrizione().equalsIgnoreCase("cena") || optionalTipoPasto.get().getDescrizione().equalsIgnoreCase("colazione"))
					if(tpDTO.getOrarioAl() == null || tpDTO.getOrarioDal() == null )
						throw new GesevException("Impossibile creare la mensa, orario tipo pasto non valido", HttpStatus.BAD_REQUEST);
			}

			TipoPasto tipoPastoMom = optionalTipoPasto.get();

			//Assegnazione
			AssTipoPastoMensa assTipoPastoMensa = new AssTipoPastoMensa();

			assTipoPastoMensa.setMensa(mensaMom);
			assTipoPastoMensa.setTipoPasto(tipoPastoMom);

			//Conversione Orari
			try
			{
				assTipoPastoMensa.setOrarioDal(ControlloData.controlloTempo(tpDTO.getOrarioDal()));
				assTipoPastoMensa.setOrarioAl(ControlloData.controlloTempo(tpDTO.getOrarioAl()));
				if(optionalTipoPasto.get().getDescrizione().equalsIgnoreCase("pranzo"))
					assTipoPastoMensa.setOraFinePrenotazione(ControlloData.controlloTempo(tpDTO.getOraFinePrenotazione()));
			}
			catch(Exception exc)
			{
				logger.info("Impossibile creare la mensa, conversione campi Tipo Pasto fallita" + exc);
				throw new GesevException("Impossibile creare la mensa, conversione campi Tipo Pasto fallita" + exc, HttpStatus.BAD_REQUEST);
			}

			listaAssTipoPastoMensas.add(assTipoPastoMensa);
		}

		//Salvataggio ASS_TIPO_PASTO_MENSA su DB
		for(AssTipoPastoMensa aTPM : listaAssTipoPastoMensas)
		{
			assTipoPastoMensaRepository.save(aTPM);
		}

		//Lista Associativa Mensa Tipo Locale
		List<AssMensaTipoLocale> listaAssMensaTipoLocale = new ArrayList<>();

		for(TipoLocaleDTO tpDTO : listaTipoLocaleDTO)
		{
			//Controllo campi
			if(tpDTO.getNumeroLocali() < 0 || tpDTO.getSuperfice() <= 0 )
			{
				logger.info("Impossibile creare la mensa, campi Tipo Locale non validi");
				throw new GesevException("Impossibile creare la mensa, campi Tipo Locale non validi", HttpStatus.BAD_REQUEST);
			}

			//Controllo Esistenza
			Optional<TipoLocale> optionalTipoLocale = tipoLocaliRepository.findById(tpDTO.getCodiceTipoLocale());
			if(!optionalTipoLocale.isPresent())
			{
				logger.info("Impossibile creare la mensa, Tipo Locale non presente");
				throw new GesevException("Impossibile creare la mensa, Tipo Locale non presente", HttpStatus.BAD_REQUEST);
			}

			AssMensaTipoLocale assMensaTipoLocale = new AssMensaTipoLocale();

			//Associazione
			assMensaTipoLocale.setMensa(mensaMom);
			assMensaTipoLocale.setTipoLocale(optionalTipoLocale.get());
			String dataFine = "9999-01-01";
			assMensaTipoLocale.setDataFine(simpleDateFormat.parse(dataFine));
			Date today = new Date();
			assMensaTipoLocale.setDataInizio(today);
			assMensaTipoLocale.setSuperficie(tpDTO.getSuperfice());
			assMensaTipoLocale.setNumeroLocali(tpDTO.getNumeroLocali());
			assMensaTipoLocale.setNote(tpDTO.getDescrizioneTipoLocale());

			listaAssMensaTipoLocale.add(assMensaTipoLocale);
		}

		for(AssMensaTipoLocale aMTL : listaAssMensaTipoLocale)
		{
			assMensaTipoLocaleRepository.save(aMTL);
		}

		return mensa.getCodiceMensa();
	}

	/* Aggiorna una Mensa */
	@Override
	@Transactional
	public int updateMensa(int idMensa, Mensa mensa, List<TipoLocaleDTO> listaTipoLocaleDTO,
			List<TipoPastoDTO> listaTipoPastoDTO, List<ServizioEventoDTO> listaServizioEventoDTO,
			int codiceTipoFormaVettovagliamento, int idEnte) throws ParseException 
	{
		logger.info("Accesso a updateMensa, classe MensaDAOImpl");	
		logger.info("Inizio controlli in corso...");	
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateFormat);

		//Controllo Mensa
		Optional<Mensa> optionalMensa = mensaRepository.findById(idMensa);
		if(!optionalMensa.isPresent())
		{
			logger.info("Impossibile modificare la mensa, mensa non presente");
			throw new GesevException("Impossibile modificare la mensa, mensa non presente", HttpStatus.BAD_REQUEST);
		}
		Mensa mensaDaDB = optionalMensa.get();
		mensa.setCodiceMensa(mensaDaDB.getCodiceMensa());

		//Controlli Ente
		Optional<Ente> optionalEnte = enteRepository.findById(idEnte);
		if(!optionalEnte.isPresent())
		{
			logger.info("Impossibile modificare la mensa, Ente non valido");
			throw new GesevException("Impossibile modificare la mensa, ente non valido", HttpStatus.BAD_REQUEST);
		}
		mensa.setEnte(optionalEnte.get());

		//Controlli TipoVettovagliamento
		Optional<TipoFormaVettovagliamento> optionalTipoFormaVett = tipoFormaVettovagliamentoRepository.findById(codiceTipoFormaVettovagliamento);
		if(!optionalTipoFormaVett.isPresent())
		{
			logger.info("Impossibile creare la mensa, Tipo Vettovagliamento non valido");
			throw new GesevException("Impossibile creare la mensa, Tipo Vettovagliamento non valido", HttpStatus.BAD_REQUEST);
		}
		mensa.setTipoFormaVettovagliamento(optionalTipoFormaVett.get());

		//Lista Servzi Festivi
		if(listaServizioEventoDTO == null || listaServizioEventoDTO.isEmpty())
		{
			logger.info("Impossibile creare la mensa, non ci sono Servizi Eventi validi");
			throw new GesevException("Impossibile creare la mensa, non ci sono Servizi Eventi validi", HttpStatus.BAD_REQUEST);
		}

		List<ServizioEvento> listaServizioEvento = new ArrayList<>();

		for(ServizioEventoDTO lsDTO : listaServizioEventoDTO)
		{
			ServizioEvento evento = new ServizioEvento();

			//Controllo Campi
			if(StringUtils.isBlank(lsDTO.getDescrizioneServizioEvento()) || StringUtils.isBlank(lsDTO.getDataServizioEvento()))
			{
				logger.info("Impossibile creare la mensa, campi Servizi Evento non validi");
				throw new GesevException("Impossibile creare la mensa, campi Servizi Evento non validi", HttpStatus.BAD_REQUEST);
			}

			//Conversione
			try 
			{
				evento.setDataServizioEvento(simpleDateFormat.parse(lsDTO.getDataServizioEvento()));
				evento.setDescrizioneServizioEvento(lsDTO.getDescrizioneServizioEvento());
			}
			catch(GesevException exc)
			{
				logger.info("Impossibile creare la mensa, conversione campi Servizi Evento fallita" + exc);
				throw new GesevException("Impossibile creare la mensa, conversione campi Servizi Evento fallita" + exc, HttpStatus.BAD_REQUEST);
			}

			listaServizioEvento.add(evento);
		}

		mensa.setListaServizioEvento(listaServizioEvento);
		mensaRepository.save(mensa);
		Mensa mensaMom = mensa;

		assTipoPastoMensaRepository.cancellaPerMensaFK(idMensa);
		assMensaTipoLocaleRepository.cancellaPerMensaFK(idMensa);

		//Lista Associativa Tipo Pasto Mensa
		if(listaTipoPastoDTO == null || listaTipoPastoDTO.isEmpty())
		{
			logger.info("Impossibile creare la mensa, non ci sono Locali validi");
			throw new GesevException("Impossibile creare la mensa, non ci sono Locali validi", HttpStatus.BAD_REQUEST);
		}

		List<AssTipoPastoMensa> listaAssTipoPastoMensas = new ArrayList<>();

		for(TipoPastoDTO tpDTO : listaTipoPastoDTO)
		{
//			//Controllo campi
//			if(StringUtils.isBlank(tpDTO.getOraFinePrenotazione()) || StringUtils.isBlank(tpDTO.getOrarioAl()) || StringUtils.isBlank(tpDTO.getOrarioDal()))
//			{
//				logger.info("Impossibile creare la mensa, campi Tipo Pasto non validi");
//				throw new GesevException("Impossibile creare la mensa, campi Tipo Pasto non validi", HttpStatus.BAD_REQUEST);
//			}

			//Controllo esistenza Tipo Pasto
			Optional<TipoPasto> optionalTipoPasto = tipoPastoRepository.findById(tpDTO.getCodiceTipoPasto());
			if(!optionalTipoPasto.isPresent())
			{
				logger.info("Impossibile creare la mensa, Tipo Pasto non presente");
				throw new GesevException("Impossibile creare la mensa, Tipo Pasto non presente", HttpStatus.BAD_REQUEST);
			}

			//Controllo Orari 
			if(optionalTipoPasto.isPresent())
			{
				if(optionalTipoPasto.get().getDescrizione().equalsIgnoreCase("pranzo"))
					if(tpDTO.getOrarioAl() == null || tpDTO.getOrarioDal() == null || tpDTO.getOraFinePrenotazione() == null )
						throw new GesevException("Impossibile creare la mensa, orario tipo pasto non valido", HttpStatus.BAD_REQUEST);

				if(optionalTipoPasto.get().getDescrizione().equalsIgnoreCase("cena") || optionalTipoPasto.get().getDescrizione().equalsIgnoreCase("colazione"))
					if(tpDTO.getOrarioAl() == null || tpDTO.getOrarioDal() == null )
						throw new GesevException("Impossibile creare la mensa, orario tipo pasto non valido", HttpStatus.BAD_REQUEST);
			}

			TipoPasto tipoPastoMom = optionalTipoPasto.get();

			//Assegnazione
			AssTipoPastoMensa assTipoPastoMensa = new AssTipoPastoMensa();

			assTipoPastoMensa.setMensa(mensaMom);
			assTipoPastoMensa.setTipoPasto(tipoPastoMom);

			//Conversione Orari
			try
			{
				assTipoPastoMensa.setOrarioDal(ControlloData.controlloTempo(tpDTO.getOrarioDal()));
				assTipoPastoMensa.setOrarioAl(ControlloData.controlloTempo(tpDTO.getOrarioAl()));
				if(optionalTipoPasto.get().getDescrizione().equalsIgnoreCase("pranzo"))
					assTipoPastoMensa.setOraFinePrenotazione(ControlloData.controlloTempo(tpDTO.getOraFinePrenotazione()));
			}
			catch(Exception exc)
			{
				logger.info("Impossibile creare la mensa, conversione campi Tipo Pasto fallita" + exc);
				throw new GesevException("Impossibile creare la mensa, conversione campi Tipo Pasto fallita" + exc, HttpStatus.BAD_REQUEST);
			}

			listaAssTipoPastoMensas.add(assTipoPastoMensa);
		}

		//Salvataggio ASS_TIPO_PASTO_MENSA su DB
		for(AssTipoPastoMensa aTPM : listaAssTipoPastoMensas)
		{
			assTipoPastoMensaRepository.save(aTPM);
		}

		//Lista Associativa Mensa Tipo Locale
		List<AssMensaTipoLocale> listaAssMensaTipoLocale = new ArrayList<>();

		for(TipoLocaleDTO tpDTO : listaTipoLocaleDTO)
		{
			//Controllo campi
			if(tpDTO.getNumeroLocali() < 0 || tpDTO.getSuperfice() <= 0 )
			{
				logger.info("Impossibile creare la mensa, campi Tipo Locale non validi");
				throw new GesevException("Impossibile creare la mensa, campi Tipo Locale non validi", HttpStatus.BAD_REQUEST);
			}

			//Controllo Esistenza
			Optional<TipoLocale> optionalTipoLocale = tipoLocaliRepository.findById(tpDTO.getCodiceTipoLocale());
			if(!optionalTipoLocale.isPresent())
			{
				logger.info("Impossibile creare la mensa, Tipo Locale non presente");
				throw new GesevException("Impossibile creare la mensa, Tipo Locale non presente", HttpStatus.BAD_REQUEST);
			}

			AssMensaTipoLocale assMensaTipoLocale = new AssMensaTipoLocale();

			//Associazione
			assMensaTipoLocale.setMensa(mensaMom);
			assMensaTipoLocale.setTipoLocale(optionalTipoLocale.get());
			String dataFine = "9999-01-01";
			assMensaTipoLocale.setDataFine(simpleDateFormat.parse(dataFine));
			Date today = new Date();
			assMensaTipoLocale.setDataInizio(today);
			assMensaTipoLocale.setSuperficie(tpDTO.getSuperfice());
			assMensaTipoLocale.setNumeroLocali(tpDTO.getNumeroLocali());
			assMensaTipoLocale.setNote(tpDTO.getDescrizioneTipoLocale());

			listaAssMensaTipoLocale.add(assMensaTipoLocale);
		}

		for(AssMensaTipoLocale aMTL : listaAssMensaTipoLocale)
		{
			assMensaTipoLocaleRepository.save(aMTL);
		}

		return mensa.getCodiceMensa();
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
		Mensa mensa = optionalMensa.isPresent() ? optionalMensa.get() : null;
		return mensa;
	}

	/* Invio File */
	@Override
	public Mensa getFile(int idMensa) 
	{
		Optional<Mensa> optionalMensa = mensaRepository.findByCodiceMensa(idMensa);
		Mensa mensa = optionalMensa.isPresent() ? optionalMensa.get() : null;
		return mensa;
	}

	/* Cerca Mense per Id Ente */
	@Override
	public List<Mensa> getMensaPerEnte(int idEnte) 
	{
		logger.info("Accesso a getMensaPerEnte classe MensaDAOImpl");
		Optional<Ente> optionalEnte = enteRepository.findById(idEnte);
		if(!optionalEnte.isPresent())
			throw new GesevException("Ente non presente" ,  HttpStatus.BAD_REQUEST);

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

	/* Lista Ente Filtrato Per Mensa */
	@Override
	public List<Ente> getEntiFiltratiPerMensa(int idMensa) 
	{
		logger.info("Accesso a getEntiFiltratiPerMensa classe MensaDAOImpl");
		return null;
	}

	//----------------------------------------------------------------------------

	/* TipoPasto per ID */
	@Override
	public Optional<TipoPasto> getTipoPastoPerId(int idTipoPasto)
	{
		logger.info("Accesso a getTipoPastoPerId classe MensaDAOImpl");
		return tipoPastoRepository.findById(idTipoPasto);
	}

	/* Lista Tipo Pasto da ID Associativa */
	@Override
	public List<TipoPasto> getTipoPastoPerLista(List<Integer> codiciPasto) 
	{
		List<TipoPasto> listaTipoPasto = new ArrayList<>();
		for(int number : codiciPasto)
		{
			TipoPasto tipoPasto = new TipoPasto();
			Optional<TipoPasto> optional = tipoPastoRepository.findById(number);
			tipoPasto = optional.isPresent() ? optional.get() : null;
			listaTipoPasto.add(tipoPasto);	
		}
		return listaTipoPasto;
	}

	/* Servizio Evento per mensa */
	@Override
	public List<ServizioEvento> getServizioEventoPerMensa(int idMensa) 
	{
		logger.info("Accesso a getServizioEventoPerMensa classe MensaDAOImpl");
		List<ServizioEvento> listaServizioEvento = servizioEventoRepository.cercaPerMensa(idMensa);

		return listaServizioEvento;
	}

}
