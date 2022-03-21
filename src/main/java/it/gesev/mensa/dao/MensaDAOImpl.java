package it.gesev.mensa.dao;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import it.gesev.mensa.dto.RicercaColonnaDTO;
import it.gesev.mensa.dto.ServizioEventoDTO;
import it.gesev.mensa.dto.TipoDietaDTO;
import it.gesev.mensa.dto.TipoLocaleDTO;
import it.gesev.mensa.dto.TipoPastoDTO;
import it.gesev.mensa.entity.AssMensaTipoDieta;
import it.gesev.mensa.entity.AssMensaTipoLocale;
import it.gesev.mensa.entity.AssTipoPastoMensa;
import it.gesev.mensa.entity.Ente;
import it.gesev.mensa.entity.Mensa;
import it.gesev.mensa.entity.ServizioEvento;
import it.gesev.mensa.entity.TipoDieta;
import it.gesev.mensa.entity.TipoFormaVettovagliamento;
import it.gesev.mensa.entity.TipoLocale;
import it.gesev.mensa.entity.TipoPasto;
import it.gesev.mensa.enums.ColonneMensaEnum;
import it.gesev.mensa.exc.GesevException;
import it.gesev.mensa.repository.AssMensaTipoDietaRepository;
import it.gesev.mensa.repository.AssMensaTipoLocaleRepository;
import it.gesev.mensa.repository.AssTipoPastoMensaRepository;
import it.gesev.mensa.repository.EnteRepository;
import it.gesev.mensa.repository.MensaRepository;
import it.gesev.mensa.repository.ServizioEventoRepository;
import it.gesev.mensa.repository.TipoDietaRepository;
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

	@Autowired
	private TipoDietaRepository tipoDietaRepository;

	@Autowired
	private AssMensaTipoDietaRepository assMensaTipoDietaRepository;

	@PersistenceContext
	EntityManager entityManager;

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
			List<ServizioEventoDTO> listaServizioEventoDTO, List<TipoDietaDTO> listaTipoDietaDTO, int codiceTipoFormaVettovagliamento, int idEnte) throws ParseException	
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
		if(listaServizioEventoDTO != null || !listaServizioEventoDTO.isEmpty())
		{	
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
					logger.info("Impossibile creare la mensa, conversione campi Servizi Evento fallita", exc);
					throw new GesevException("Impossibile creare la mensa, conversione campi Servizi Evento fallita", HttpStatus.BAD_REQUEST);
				}
				evento.setMensa(mensa);
				listaServizioEvento.add(servizioEventoRepository.save(evento));


			}

			mensa.setListaServizioEvento(listaServizioEvento);
		}

		Mensa mensaMom = mensaRepository.save(mensa);


		//Lista Associativa Tipo Pasto Mensa
		if(listaTipoPastoDTO != null && !listaTipoPastoDTO.isEmpty())
		{

			List<AssTipoPastoMensa> listaAssTipoPastoMensas = new ArrayList<>();
			boolean controlloPranzo = false;
			List<Integer> listaCodiciTipoPasti = new ArrayList<>();

			//Recupero TipoPasto dal DB
			for(TipoPastoDTO tpDTO : listaTipoPastoDTO)
			{	
				listaCodiciTipoPasti.add(tpDTO.getCodiceTipoPasto());

				if(tpDTO.getDescrizione().equalsIgnoreCase("pranzo"))		
					controlloPranzo = true;
			}

			//Controlli
			if(controlloPranzo == false)
				throw new GesevException("Impossibile creare la mensa, i dati del pranzo sono obbligatori");
			
			List<TipoPasto> listaTipoPasto = tipoPastoRepository.findByCodiceTipoPastoIn(listaCodiciTipoPasti);

			if(listaTipoPasto.size() != listaTipoPastoDTO.size())
				throw new GesevException("Impossibile creare la mensa, errore nella creazione del Tipo Pasto", HttpStatus.BAD_REQUEST);

			//Ciclo listaTipoPastoDTO 
			for(TipoPastoDTO tpDTO : listaTipoPastoDTO)
			{		

				//				//Controllo esistenza Tipo Pasto
				//				Optional<TipoPasto> optionalTipoPasto = tipoPastoRepository.findById(tpDTO.getCodiceTipoPasto());
				//				if(!optionalTipoPasto.isPresent())
				//				{
				//					logger.info("Impossibile creare la mensa, Tipo Pasto non presente");
				//					throw new GesevException("Impossibile creare la mensa, Tipo Pasto non presente", HttpStatus.BAD_REQUEST);
				//				}
				//				

				//Controllo Orari 
				if(tpDTO.getDescrizione().equalsIgnoreCase("pranzo"))
				{
					if(tpDTO.getOrarioAl() == null || tpDTO.getOrarioDal() == null || tpDTO.getOraFinePrenotazione() == null )
						throw new GesevException("Impossibile creare la mensa, orario tipo pasto non valido", HttpStatus.BAD_REQUEST);
				}
				
				if(tpDTO.getDescrizione().equalsIgnoreCase("cena") || tpDTO.getDescrizione().equalsIgnoreCase("colazione"))
				{
					if(tpDTO.getOrarioAl() == null || tpDTO.getOrarioDal() == null )
						throw new GesevException("Impossibile creare la mensa, orario tipo pasto non valido", HttpStatus.BAD_REQUEST);
				}
				
				//Assegnazione
				AssTipoPastoMensa assTipoPastoMensa = new AssTipoPastoMensa();
				for(TipoPasto tp : listaTipoPasto)
				{
					if(tp.getCodiceTipoPasto() == tpDTO.getCodiceTipoPasto())
					{
						assTipoPastoMensa.setTipoPasto(tp);
						break;
					}
				}

				assTipoPastoMensa.setMensa(mensaMom);

				//Conversione Orari
				try
				{
					assTipoPastoMensa.setOrarioDal(ControlloData.controlloTempo(tpDTO.getOrarioDal()));
					assTipoPastoMensa.setOrarioAl(ControlloData.controlloTempo(tpDTO.getOrarioAl()));

					if(assTipoPastoMensa.getOrarioDal().isAfter(assTipoPastoMensa.getOrarioAl()) || 
							assTipoPastoMensa.getOrarioDal().equals(assTipoPastoMensa.getOrarioAl()))
						throw new GesevException("Impossibile creare la mensa, orari non validi", HttpStatus.BAD_REQUEST);

					if(tpDTO.getDescrizione().equalsIgnoreCase("pranzo"))
					{				
						assTipoPastoMensa.setOraFinePrenotazione(ControlloData.controlloTempo(tpDTO.getOraFinePrenotazione()));
						if(assTipoPastoMensa.getOrarioAl().isBefore(assTipoPastoMensa.getOraFinePrenotazione()) ||
								assTipoPastoMensa.getOraFinePrenotazione().isAfter(assTipoPastoMensa.getOrarioDal()))
							throw new GesevException("Impossibile creare la mensa, orari non validi", HttpStatus.BAD_REQUEST);
					}
				}
				catch(Exception exc)
				{
					logger.info("Impossibile creare la mensa, orari Tipo Pasto non validi", exc);
					throw new GesevException("Impossibile creare la mensa, orari Tipo Pasto non validi", HttpStatus.BAD_REQUEST);
				}

				listaAssTipoPastoMensas.add(assTipoPastoMensa);
			}

			//Salvataggio ASS_TIPO_PASTO_MENSA su DB
			for(AssTipoPastoMensa aTPM : listaAssTipoPastoMensas)
			{
				assTipoPastoMensaRepository.save(aTPM);
			}
		}
		else
		{
			logger.info("Impossibile creare la mensa, la lista tipo pasto è vuota");
			throw new GesevException("Impossibile creare la mensa, la lista tipo pasto è vuota", HttpStatus.BAD_REQUEST);
		}

		//Lista Associativa Mensa Tipo Locale
		if(listaTipoLocaleDTO != null || !listaTipoLocaleDTO.isEmpty())
		{

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
				assMensaTipoLocale.setNote(tpDTO.getNote());

				listaAssMensaTipoLocale.add(assMensaTipoLocale);
			}

			for(AssMensaTipoLocale aMTL : listaAssMensaTipoLocale)
			{
				assMensaTipoLocaleRepository.save(aMTL);
			}
		}

		//Lista Associativa Mensa Tipo Dieta
		if(listaTipoDietaDTO != null || !listaTipoDietaDTO.isEmpty())
		{
			List<AssMensaTipoDieta> listaAssMensaTipoDieta = new ArrayList<>();

			for(TipoDietaDTO tdDTO : listaTipoDietaDTO)
			{

				//Controllo Esistenza
				Optional<TipoDieta> optionalTipoDieta = tipoDietaRepository.findById(tdDTO.getIdTipoDieta());
				if(!optionalTipoDieta.isPresent())
				{
					logger.info("Impossibile creare la mensa, Tipo Dieta non presente");
					throw new GesevException("Impossibile creare la mensa, Tipo Dieta non presente", HttpStatus.BAD_REQUEST);
				}

				AssMensaTipoDieta assMensaTipoDieta = new AssMensaTipoDieta();

				//Associazione
				assMensaTipoDieta.setMensa(mensaMom);
				assMensaTipoDieta.setTipoDieta(optionalTipoDieta.get());

				listaAssMensaTipoDieta.add(assMensaTipoDieta);
			}

			for(AssMensaTipoDieta aMTD : listaAssMensaTipoDieta)
			{
				assMensaTipoDietaRepository.save(aMTD);
			}
		}

		return mensa.getCodiceMensa();
	}

	/* Aggiorna una Mensa */
	@Override
	@Transactional
	public int updateMensa(int idMensa, Mensa mensa, List<TipoLocaleDTO> listaTipoLocaleDTO,
			List<TipoPastoDTO> listaTipoPastoDTO, List<ServizioEventoDTO> listaServizioEventoDTO, List<TipoDietaDTO> listaTipoDietaDTO,
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

		servizioEventoRepository.cancellaPerMensaFK(idMensa);

		//Lista Servzi Festivi
		if(listaServizioEventoDTO != null || !listaServizioEventoDTO.isEmpty())
		{
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
					logger.info("Impossibile creare la mensa, conversione campi Servizi Evento fallita", exc);
					throw new GesevException("Impossibile creare la mensa, conversione campi Servizi Evento fallita", HttpStatus.BAD_REQUEST);
				}

				evento.setMensa(mensa);
				listaServizioEvento.add(servizioEventoRepository.save(evento));
			}
			mensa.setListaServizioEvento(listaServizioEvento);
		}

		Mensa mensaMom = mensaRepository.save(mensa);

		assTipoPastoMensaRepository.cancellaPerMensaFK(idMensa);
		assMensaTipoLocaleRepository.cancellaPerMensaFK(idMensa);
		assMensaTipoDietaRepository.cancellaPerMensaFK(idMensa);

		//Lista Associativa Tipo Pasto Mensa
		if(listaTipoPastoDTO != null && !listaTipoPastoDTO.isEmpty())
		{

			List<AssTipoPastoMensa> listaAssTipoPastoMensas = new ArrayList<>();
			boolean controlloPranzo = false;
			List<Integer> listaCodiciTipoPasti = new ArrayList<>();

			//Recupero TipoPasto dal DB
			for(TipoPastoDTO tpDTO : listaTipoPastoDTO)
			{	
				listaCodiciTipoPasti.add(tpDTO.getCodiceTipoPasto());

				if(tpDTO.getDescrizione().equalsIgnoreCase("pranzo"))		
					controlloPranzo = true;
			}
			
			//Controlli
			if(controlloPranzo == false)
				throw new GesevException("Impossibile creare la mensa, i dati del pranzo sono obbligatori");

			List<TipoPasto> listaTipoPasto = tipoPastoRepository.findByCodiceTipoPastoIn(listaCodiciTipoPasti);

			if(listaTipoPasto.size() != listaTipoPastoDTO.size())
				throw new GesevException("Impossibile creare la mensa, errore nella creazione del Tipo Pasto", HttpStatus.BAD_REQUEST);

			//Ciclo listaTipoPastoDTO 
			for(TipoPastoDTO tpDTO : listaTipoPastoDTO)
			{		

				//				//Controllo esistenza Tipo Pasto
				//				Optional<TipoPasto> optionalTipoPasto = tipoPastoRepository.findById(tpDTO.getCodiceTipoPasto());
				//				if(!optionalTipoPasto.isPresent())
				//				{
				//					logger.info("Impossibile creare la mensa, Tipo Pasto non presente");
				//					throw new GesevException("Impossibile creare la mensa, Tipo Pasto non presente", HttpStatus.BAD_REQUEST);
				//				}
				//				

				//Controllo Orari 
				if(tpDTO.getDescrizione().equalsIgnoreCase("pranzo"))
					if(tpDTO.getOrarioAl() == null || tpDTO.getOrarioDal() == null || tpDTO.getOraFinePrenotazione() == null )
						throw new GesevException("Impossibile creare la mensa, orario tipo pasto non valido", HttpStatus.BAD_REQUEST);

				if(tpDTO.getDescrizione().equalsIgnoreCase("cena") || tpDTO.getDescrizione().equalsIgnoreCase("colazione"))
					if(tpDTO.getOrarioAl() == null || tpDTO.getOrarioDal() == null )
						throw new GesevException("Impossibile creare la mensa, orario tipo pasto non valido", HttpStatus.BAD_REQUEST);

				//Assegnazione
				AssTipoPastoMensa assTipoPastoMensa = new AssTipoPastoMensa();
				for(TipoPasto tp : listaTipoPasto)
				{
					if(tp.getCodiceTipoPasto() == tpDTO.getCodiceTipoPasto())
					{
						assTipoPastoMensa.setTipoPasto(tp);
						break;
					}
				}

				assTipoPastoMensa.setMensa(mensaMom);

				//Conversione Orari
				try
				{
					assTipoPastoMensa.setOrarioDal(ControlloData.controlloTempo(tpDTO.getOrarioDal()));
					assTipoPastoMensa.setOrarioAl(ControlloData.controlloTempo(tpDTO.getOrarioAl()));

					if(assTipoPastoMensa.getOrarioDal().isAfter(assTipoPastoMensa.getOrarioAl()) || 
							assTipoPastoMensa.getOrarioDal().equals(assTipoPastoMensa.getOrarioAl()))
						throw new GesevException("Impossibile creare la mensa, orari non validi", HttpStatus.BAD_REQUEST);

					if(tpDTO.getDescrizione().equalsIgnoreCase("pranzo"))
					{				
						assTipoPastoMensa.setOraFinePrenotazione(ControlloData.controlloTempo(tpDTO.getOraFinePrenotazione()));
						if(assTipoPastoMensa.getOrarioAl().isBefore(assTipoPastoMensa.getOraFinePrenotazione()) ||
								assTipoPastoMensa.getOraFinePrenotazione().isAfter(assTipoPastoMensa.getOrarioDal()))
							throw new GesevException("Impossibile creare la mensa, orari non validi", HttpStatus.BAD_REQUEST);
					}
				}
				catch(Exception exc)
				{
					logger.info("Impossibile creare la mensa, orari Tipo Pasto non validi", exc);
					throw new GesevException("Impossibile creare la mensa, orari Tipo Pasto non validi", HttpStatus.BAD_REQUEST);
				}

				listaAssTipoPastoMensas.add(assTipoPastoMensa);
			}

			//Salvataggio ASS_TIPO_PASTO_MENSA su DB
			for(AssTipoPastoMensa aTPM : listaAssTipoPastoMensas)
			{
				assTipoPastoMensaRepository.save(aTPM);
			}
		}
		else
		{
			logger.info("Impossibile creare la mensa, la lista tipo pasto è vuota");
			throw new GesevException("Impossibile creare la mensa, la lista tipo pasto è vuota", HttpStatus.BAD_REQUEST);
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
			assMensaTipoLocale.setNote(tpDTO.getNote());

			listaAssMensaTipoLocale.add(assMensaTipoLocale);
		}

		for(AssMensaTipoLocale aMTL : listaAssMensaTipoLocale)
		{
			assMensaTipoLocaleRepository.save(aMTL);
		}

		//Lista Associativa Mensa Tipo Dieta
		if(listaTipoDietaDTO != null || !listaTipoDietaDTO.isEmpty())
		{
			List<AssMensaTipoDieta> listaAssMensaTipoDieta = new ArrayList<>();

			for(TipoDietaDTO tdDTO : listaTipoDietaDTO)
			{

				//Controllo Esistenza
				Optional<TipoDieta> optionalTipoDieta = tipoDietaRepository.findById(tdDTO.getIdTipoDieta());
				if(!optionalTipoDieta.isPresent())
				{
					logger.info("Impossibile creare la mensa, Tipo Dieta non presente");
					throw new GesevException("Impossibile creare la mensa, Tipo Dieta non presente", HttpStatus.BAD_REQUEST);
				}

				AssMensaTipoDieta assMensaTipoDieta = new AssMensaTipoDieta();

				//Associazione
				assMensaTipoDieta.setMensa(mensaMom);
				assMensaTipoDieta.setTipoDieta(optionalTipoDieta.get());

				listaAssMensaTipoDieta.add(assMensaTipoDieta);
			}

			for(AssMensaTipoDieta aMTD : listaAssMensaTipoDieta)
			{
				assMensaTipoDietaRepository.save(aMTD);
			}
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
			if(StringUtils.isBlank(mensa.getDescrizioneMensa()))
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
		logger.info("Controllo presenza mensa in corso...");
		Optional<Mensa> optionalMensa = mensaRepository.findById(idMensa);
		if(!optionalMensa.isPresent())
		{
			logger.info("Errore mensa non presente");
			throw new GesevException("Errore mensa non presente", HttpStatus.BAD_REQUEST);
		}

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
		logger.info("Controllo presenza mensa in corso...");
		Optional<Mensa> optionalMensa = mensaRepository.findById(idMensa);
		if(!optionalMensa.isPresent())
		{
			logger.info("Errore mensa non presente");
			throw new GesevException("Errore mensa non presente", HttpStatus.BAD_REQUEST);
		}

		logger.info("Accesso a getServizioEventoPerMensa classe MensaDAOImpl");
		List<ServizioEvento> listaServizioEvento = servizioEventoRepository.cercaPerMensa(idMensa);

		return listaServizioEvento;
	}

	/* leggi Tipo Dieta */
	@Override
	public List<TipoDieta> getAllTipoDieta() 
	{
		logger.info("Accesso a getAllTipoDieta classe MensaDAOImpl");
		List<TipoDieta> listaTipoDieta = tipoDietaRepository.findAll();
		return listaTipoDieta;
	}

	/* Tipo Dieta per Mensa */
	@Override
	public List<TipoDieta> getTipoDietaPerMensa(int idMensa) 
	{
		logger.info("Controllo presenza mensa in corso...");
		Optional<Mensa> optionalMensa = mensaRepository.findById(idMensa);
		if(!optionalMensa.isPresent())
		{
			logger.info("Errore mensa non presente");
			throw new GesevException("Errore mensa non presente", HttpStatus.BAD_REQUEST);
		}

		List<AssMensaTipoDieta> listaAssMensaTipoDieta = assMensaTipoDietaRepository.cercaPerMensa(idMensa);
		List<TipoDieta> listaTipoDieta = new ArrayList<>();
		for(AssMensaTipoDieta aMTP : listaAssMensaTipoDieta)
		{
			TipoDieta tipoDieta = new TipoDieta();
			int codiceTipoDieta = aMTP.getTipoDieta().getIdTipoDieta();
			Optional<TipoDieta> optionalTipoDieta = tipoDietaRepository.findById(codiceTipoDieta);
			if(!optionalTipoDieta.isPresent())
				throw new GesevException("Errore codice tipo dieta non presente", HttpStatus.BAD_REQUEST);

			tipoDieta = optionalTipoDieta.get();
			listaTipoDieta.add(tipoDieta);
		}
		return listaTipoDieta;
	}

	/* Ricerca Colonne*/
	@Override
	public List<Mensa> ricercaMense(int idEnte, List<RicercaColonnaDTO> colonne) 
	{
		logger.info("Accesso al servizio ricercaMense in MensaDAOImpl");
		//L'obj che crea le istruzioni and or etc
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		//Query 
		CriteriaQuery<Mensa> criteriaQuery = criteriaBuilder.createQuery(Mensa.class);
		//From
		Root<Mensa> mensaRoot = criteriaQuery.from(Mensa.class);
		//Join, metto nel join il campo dell'entità 
		Join<Mensa, Ente> enteJoin = mensaRoot.join("ente");
		Join<Mensa, TipoFormaVettovagliamento> vettovagliamentoJoin = mensaRoot.join("tipoFormaVettovagliamento");

		//Where, condizioni che possono o non possono esserci, in questo caso per forza
		Predicate finalPredicate = criteriaBuilder.equal(enteJoin.get("idEnte"), idEnte);

		for(RicercaColonnaDTO rcd : colonne)
		{
			try
			{
				if(StringUtils.isBlank(rcd.getColonna()) || StringUtils.isBlank(rcd.getValue())	)
					throw new Exception("Colonna o valore non validi");

				//Controllo che la colonna sia presente nella enumerazione e se non va entra nel catch
				ColonneMensaEnum enumerazione = ColonneMensaEnum.valueOf(rcd.getColonna().toUpperCase());

				switch(enumerazione)
				{
				case DESCRIZIONE:
					//Faccio l'upper case del valore che sta nella colonna della mensa
					Expression<String> espressioneDescrizione = criteriaBuilder.upper(mensaRoot.get(enumerazione.getColonna()));
					finalPredicate = criteriaBuilder.and(finalPredicate, criteriaBuilder.like(espressioneDescrizione, rcd.getValue().toUpperCase() + "%"));
					break;
				case ENTE:
					Expression<String> espressioneDescrizioneEnte = criteriaBuilder.upper(enteJoin.get(enumerazione.getColonna()));
					finalPredicate = criteriaBuilder.and(finalPredicate, criteriaBuilder.like(espressioneDescrizioneEnte, rcd.getValue().toUpperCase() + "%"));
					break;
				case TIPO_VETTOVAGLIAMENTO:
					finalPredicate = criteriaBuilder.and(finalPredicate, criteriaBuilder.equal(vettovagliamentoJoin.get(enumerazione.getColonna()), 
							Integer.valueOf(rcd.getValue())));
					break;
				default:
					break;
				}
			}

			catch(Exception ex)
			{
				throw new GesevException("Nome colonna o valore non validi", HttpStatus.BAD_REQUEST);
			}
		}

		return entityManager.createQuery(criteriaQuery.where(finalPredicate)).getResultList();
	}

}
