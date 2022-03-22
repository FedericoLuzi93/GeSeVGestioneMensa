package it.gesev.mensa.service;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.commons.lang3.StringUtils;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import it.gesev.mensa.dao.MensaDAO;
import it.gesev.mensa.dto.CreaMensaDTO;
import it.gesev.mensa.dto.EnteDTO;
import it.gesev.mensa.dto.FELocaliDTO;
import it.gesev.mensa.dto.FEMensaCompletaDTO;
import it.gesev.mensa.dto.FEServizioMensaDTO;
import it.gesev.mensa.dto.FileDTO;
import it.gesev.mensa.dto.MensaDTO;
import it.gesev.mensa.dto.RicercaColonnaDTO;
import it.gesev.mensa.dto.ServizioEventoDTO;
import it.gesev.mensa.dto.TipoDietaDTO;
import it.gesev.mensa.dto.TipoFromaVettovagliamentoDTO;
import it.gesev.mensa.dto.TipoLocaleDTO;
import it.gesev.mensa.dto.TipoPastoDTO;
import it.gesev.mensa.entity.AssTipoPastoMensa;
import it.gesev.mensa.entity.Ente;
import it.gesev.mensa.entity.Mensa;
import it.gesev.mensa.entity.ServizioEvento;
import it.gesev.mensa.entity.TipoDieta;
import it.gesev.mensa.entity.TipoFormaVettovagliamento;
import it.gesev.mensa.entity.TipoLocale;
import it.gesev.mensa.entity.TipoPasto;
import it.gesev.mensa.exc.GesevException;
import it.gesev.mensa.utils.EnteMapper;
import it.gesev.mensa.utils.MensaMapper;
import it.gesev.mensa.utils.ServizioEventoMapper;
import it.gesev.mensa.utils.TipoDietaMapper;
import it.gesev.mensa.utils.TipoFormaVettovagliamentoMapper;
import it.gesev.mensa.utils.TipoLocaleMapper;
import it.gesev.mensa.utils.TipoPastoMapper;

@Service
public class MensaServiceImpl implements MensaService 
{
	@Autowired
	private MensaDAO mensaDAO;

	@Value("${gesev.data.format}")
	private String dateFormat;

	@PersistenceContext
	EntityManager entityManager;

	private static final Logger logger = LoggerFactory.getLogger(MensaServiceImpl.class);

	/* Leggi tutte le Mense */
	public List<MensaDTO> getAllMense() 
	{
		logger.info("Accesso a getAllMense, classe MensaServiceImpl");
		List<Mensa> listaMensa = mensaDAO.getAllMense();
		List<MensaDTO> listaMensaDTO = new ArrayList<>();
		logger.info("Inizio ciclo For in getAllMense, classe MensaServiceImpl");
		for(Mensa m : listaMensa)
		{
			MensaDTO mensaDTO = null;
			mensaDTO = MensaMapper.mapToDTO(m, dateFormat);
			if(m.getEnte() != null)
				mensaDTO.setDescrizioneEnte(m.getEnte().getDescrizioneEnte());
			listaMensaDTO.add(mensaDTO);
		}
		logger.info("Fine getAllMense, classe MensaServiceImpl");
		return listaMensaDTO;
	}

	/* Crea una Mensa */
	public int createMensa(CreaMensaDTO creaMensaDTO, MultipartFile multipartFile) throws ParseException, IOException 
	{
		Mensa mensa = null;
		try
		{
			logger.info("Accesso a creaMensa classe MensaServiceImpl");
			mensa = MensaMapper.mapToEntity(creaMensaDTO, dateFormat);	
			if(multipartFile != null)
				mensa.setAutorizzazioneSanitaria(multipartFile.getBytes());
			else
				mensa.setAutorizzazioneSanitaria(null);
		}
		catch(Exception exc)
		{
			logger.info("Eccezione nel servizio creaMensa", exc);
			if(exc instanceof GesevException)
				throw new GesevException(exc.getMessage(), HttpStatus.BAD_REQUEST);
			
			else
				throw exc;
		}

		logger.info("Accesso a createMensa, classe MensaServiceImpl");
		logger.info("Crezione mensa in corso...");
		return mensaDAO.createMensa(mensa, creaMensaDTO.getListaTipoLocaleDTO(), creaMensaDTO.getListaTipoPastoDTO() ,
				creaMensaDTO.getListaServizioEventoDTO(), creaMensaDTO.getListaTipoDietaDTO(), creaMensaDTO.getCodiceTipoFormaVettovagliamento(), creaMensaDTO.getIdEnte());
	}

	/* Aggiorna una Mensa */
	public int updateMensa(CreaMensaDTO creaMensaDTO, int idMensa, MultipartFile multipartFile) throws ParseException, IOException 
	{
		Mensa mensa = null;
		try
		{
			logger.info("Accesso a updateMensa, classe MensaServiceImpl");
			mensa = MensaMapper.mapToEntity(creaMensaDTO, dateFormat);	
			if(multipartFile != null)
				mensa.setAutorizzazioneSanitaria(multipartFile.getBytes());
			else
				mensa.setAutorizzazioneSanitaria(null);
		}
		catch(Exception exc)
		{
			logger.info("Eccezione nel servizio updateMensa", exc);
			if(exc instanceof GesevException)
				throw new GesevException(exc.getMessage(), HttpStatus.BAD_REQUEST);
			
			else
				throw exc;
		}
		logger.info("Modifica mensa in corso...");
		return mensaDAO.updateMensa(idMensa, mensa, creaMensaDTO.getListaTipoLocaleDTO(), creaMensaDTO.getListaTipoPastoDTO() ,
				creaMensaDTO.getListaServizioEventoDTO(), creaMensaDTO.getListaTipoDietaDTO(), creaMensaDTO.getCodiceTipoFormaVettovagliamento(), creaMensaDTO.getIdEnte());
	}

	/* Disabilita una Mensa */
	@Override
	public int disableMensa(MensaDTO mensaDTO, int idMensa) throws ParseException 
	{
		Mensa mensa = new Mensa();
		try
		{
			String dataString = mensaDTO.getDataFineServizio();
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateFormat);
			Date date = simpleDateFormat.parse(dataString);
			mensa.setDataFineServizio(date);	 			
		}
		catch(Exception exc)
		{
			logger.info("Eccezione nel servizio disabilita mensa", exc);
			if(exc instanceof GesevException)
				throw new GesevException(exc.getMessage(), HttpStatus.BAD_REQUEST);
			
			else
				throw exc;
		}
		logger.info("disabilitazione mensa in corso...");
		return mensaDAO.disableMensa(mensa, idMensa);
	}

	/* Get Singola Mensa */
	@Override
	public MensaDTO getSingolaMensa(int idMensa) 
	{
		logger.info("Accesso a getSingolaMensa classe MensaServiceImpl");
		Mensa mensa = mensaDAO.getSingolaMensa(idMensa);
		MensaDTO mensaDTO = MensaMapper.mapToDTO(mensa, dateFormat);
		logger.info("Fine getSingolaMensa classe MensaServiceImpl");
		return mensaDTO;
	}

	/* Invio del File */
	@Override
	public FileDTO getFile(int idMensa) 
	{
		Mensa mensa = mensaDAO.getFile(idMensa);
		FileDTO fileDTO = new FileDTO();
		SimpleDateFormat date = new SimpleDateFormat(dateFormat);
		fileDTO.setNomeFile(mensa.getDescrizioneMensa() + "_" + date.format(mensa.getDataAutorizzazioneSanitaria()) + ".pdf"); 
		fileDTO.setAutorizzazioneSanitaria(mensa.getAutorizzazioneSanitaria());
		return fileDTO;
	}

	/* Cerca Mense per Id Ente */
	@Override
	public List<MensaDTO> getMensaPerEnte(int idEnte)
	{
		logger.info("Accesso a getMensaPerEnte classe MensaServiceImpl");
		List<Mensa> listaMensa = mensaDAO.getMensaPerEnte(idEnte);
		List<MensaDTO> listaMensaDTO = new ArrayList<>();
		logger.info("Inizio ciclo For in getMensaPerEnte classe MensaServiceImpl");
		for(Mensa m : listaMensa)
		{
			listaMensaDTO.add(MensaMapper.mapToDTO(m, dateFormat));
		}
		logger.info("Fine getMensaPerEnte classe MensaServiceImpl");
		return listaMensaDTO;
	}

	/* --------------------------------------------------------------------------------- */

	/* Lista Locali */
	public List<TipoLocaleDTO> getAllLocali() 
	{
		logger.info("Accesso a getAllLocali, classe MensaServiceImpl");
		List<TipoLocale> listaTipoLocali = mensaDAO.getAllLocali();
		List<TipoLocaleDTO> listaTipoLocaliDTO = new ArrayList<>();
		logger.info("Inizio ciclo For in getAllLocali, classe MensaServiceImpl");
		for(TipoLocale tp : listaTipoLocali)
		{
			listaTipoLocaliDTO.add(TipoLocaleMapper.mapToDTO(tp));
		}
		logger.info("Fine getAllLocali, classe MensaServiceImpl");
		return listaTipoLocaliDTO;
	}


	/* Lista Locali per Mensa */
	@Override
	public List<FELocaliDTO> getLocaliPerMensa(int idMensa) 
	{
		logger.info("Accesso a getLocaliPerMensa, classe MensaServiceImpl");
		List<FELocaliDTO> listaFeLocaliDTO = new ArrayList<>();	
		String query = "select	tipo_locale.descrizione_tipo_locale,\r\n"
				+ "		ass_mensa_tipo_locale.superficie,\r\n"
				+ "		ass_mensa_tipo_locale.numero_locali,\r\n"
				+ "		ass_mensa_tipo_locale.note,\r\n"
				+ "		tipo_locale.codice_tipo_locale\r\n"
				+ "from	tipo_locale INNER join ass_mensa_tipo_locale\r\n"
				+ "on	codice_mensa_fk = " + idMensa + " and \r\n"
				+ "	tipo_locale.codice_tipo_locale = ass_mensa_tipo_locale.codice_tipo_locale_fk;";

		logger.info("Esecuzione query: " + query); 
		Query sumQuery = entityManager.createNativeQuery(query);
		List<Object[]> listOfResults = sumQuery.getResultList();

		for(Object[] ob : listOfResults)
		{
			FELocaliDTO feLocaliDTO = new FELocaliDTO();
			feLocaliDTO.setNomeLocale((String) ob[0]);
			Integer superfice = (Integer) ob[1];
			feLocaliDTO.setSuperfice(superfice);
			Integer numeroLocali = (Integer) ob[2];
			feLocaliDTO.setNumeroLocali(numeroLocali);;
			feLocaliDTO.setNote((String) ob[3]);
			feLocaliDTO.setCodiceTipoLocale((Integer) ob[4]);
			listaFeLocaliDTO.add(feLocaliDTO);
		}
		return listaFeLocaliDTO;
	}

	/* Lista Enti */
	@Override
	public List<EnteDTO> getAllEnti() 
	{
		logger.info("Accesso a getAllEnti, classe MensaServiceImpl");
		List<Ente> enteLista = mensaDAO.getAllEnti();
		List<EnteDTO> listaEnteDTO = new ArrayList<>();
		logger.info("Inizio ciclo for in getAllEnti, classe MensaServiceImpl");
		for(Ente e : enteLista)
		{
			listaEnteDTO.add(EnteMapper.mapToDTO(e));
		}
		logger.info("Fine getAllEnti, classe MensaServiceImpl");
		return listaEnteDTO;
	}

	/* Lista Tipo Froma Vettovagliamento */
	@Override
	public List<TipoFromaVettovagliamentoDTO> getAllTipoFormaVettovagliamento() 
	{
		logger.info("Accesso a getAllTipoFormaVettovagliamento, classe MensaServiceImpl");
		List<TipoFormaVettovagliamento> listVettovagliamento = mensaDAO.getAllTipoFormaVettovagliamento();
		List<TipoFromaVettovagliamentoDTO> listVettovagliamentoDTO = new ArrayList<>();
		logger.info("Inizio ciclo for in getAllTipoFormaVettovagliamento, classe MensaServiceImpl");
		for(TipoFormaVettovagliamento tm : listVettovagliamento)
		{
			listVettovagliamentoDTO.add(TipoFormaVettovagliamentoMapper.mapToDTO(tm));
		}
		logger.info("Fine getAllEnti, classe MensaServiceImpl");
		return listVettovagliamentoDTO;
	}

	/* Lista Tipo Pasto */
	@Override
	public List<TipoPastoDTO> getAllTipoPasto() 
	{
		logger.info("Accesso a getAllTipoPasto classe MensaServiceImpl");
		List<TipoPasto> listaTipoPasto = mensaDAO.getAllTipoPasto();
		List<TipoPastoDTO> listaTipoPastoDTO = new ArrayList<>();
		logger.info("Inizio ciclo for in getAllTipoPasto classe MensaServiceImpl");
		for(TipoPasto tp: listaTipoPasto)
		{
			listaTipoPastoDTO.add(TipoPastoMapper.mapToDTO(tp));
		}
		logger.info("Fine getAllTipoPasto classe MensaServiceImpl");
		return listaTipoPastoDTO;
	}

	/* Get Servizi per idMensa */
	@Override
	public  FEServizioMensaDTO getServiziPerMensa(int idMensa) 
	{
		logger.info("Accesso a getServiziPerMensa  classe MensaServiceImpl");
		FEServizioMensaDTO feServizioMensaDTO = new FEServizioMensaDTO();
		List<AssTipoPastoMensa> listaAssTipoPastoMensa = mensaDAO.getServiziPerMensa(idMensa);

		if(listaAssTipoPastoMensa.size()>0)
		{
			ModelMapper mapper = new ModelMapper();
			List<TipoPastoDTO> listaTipoPastoDTO = new ArrayList<>();
			Mensa mensa = null;
			DateTimeFormatter dateTime = DateTimeFormatter.ofPattern("HH:mm");

			for(AssTipoPastoMensa ass : listaAssTipoPastoMensa)
			{
				if(mensa == null)
					mensa = ass.getMensa();

				TipoPastoDTO tipoPastoDTO = (mapper.map(ass.getTipoPasto(), TipoPastoDTO.class));
				tipoPastoDTO.setOraFinePrenotazione(ass.getOraFinePrenotazione() != null ? ass.getOraFinePrenotazione().format(dateTime) : null);
				tipoPastoDTO.setOrarioAl(ass.getOrarioAl() != null ? ass.getOrarioAl().format(dateTime) : null);
				tipoPastoDTO.setOrarioDal(ass.getOrarioDal() != null ? ass.getOrarioDal().format(dateTime) : null);
				listaTipoPastoDTO.add(tipoPastoDTO);
			}

			feServizioMensaDTO.setListaTipoPastoDTO(listaTipoPastoDTO);
			SimpleDateFormat formatter = new SimpleDateFormat(this.dateFormat);

			feServizioMensaDTO.setDataFineServizio(mensa.getDataFineServizio() != null ? formatter.format(mensa.getDataFineServizio()) : null);
			feServizioMensaDTO.setDataInizioServizio(mensa.getDataInizioServizio() != null ? formatter.format(mensa.getDataInizioServizio()) : null);	
		}

		return feServizioMensaDTO;
	}

	/* Get Ente Filtrato per Mensa */
	@Override
	public List<EnteDTO> getEntiFiltratiPerMensa(int idMensa) 
	{
		logger.info("Accesso a getEntiFiltratiPerMensa classe MensaServiceImpl");
		List<Ente> enteLista = mensaDAO.getEntiFiltratiPerMensa(idMensa);
		List<EnteDTO> listaEnteDTO = new ArrayList<>();
		logger.info("Inizio ciclo for in getEntiFiltratiPerMensa classe MensaServiceImpl");
		for(Ente e : enteLista)
		{
			listaEnteDTO.add(EnteMapper.mapToDTO(e));
		}
		logger.info("Fine getEntiFiltratiPerMensa, classe MensaServiceImpl");
		return listaEnteDTO;
	}

	/* Servizio Evento per mensa */
	@Override
	public List<ServizioEventoDTO> getServizioEventoPerMensa(int idMensa) 
	{
		logger.info("Accesso a getServizioEventoPerMensa classe MensaServiceImpl");
		List<ServizioEvento> listaServizioEvento = mensaDAO.getServizioEventoPerMensa(idMensa);
		List<ServizioEventoDTO> listaServizioEventoDTO = new ArrayList<>();
		logger.info("Inizio ciclo for in getServizioEventoPerMensa classe MensaServiceImpl");
		for(ServizioEvento se : listaServizioEvento)
		{
			listaServizioEventoDTO.add(ServizioEventoMapper.mapToDTO(se, dateFormat));
		}
		logger.info("Fine getServizioEventoPerMensa, classe MensaServiceImpl");
		return listaServizioEventoDTO;
	}

	/* leggi Tipo Dieta */
	@Override
	public List<TipoDietaDTO> getAllTipoDieta()
	{
		logger.info("Accesso a getAllTipoDieta classe MensaServiceImpl");
		List<TipoDieta> listaTipoDieta = mensaDAO.getAllTipoDieta();
		List<TipoDietaDTO> listaTipoDietaDTO = new ArrayList<>();
		logger.info("Inizio ciclo for in getAllTipoDieta classe MensaServiceImpl");
		for(TipoDieta td: listaTipoDieta)
		{
			listaTipoDietaDTO.add(TipoDietaMapper.mapToDTO(td));
		}
		logger.info("Fine getAllTipoDieta classe MensaServiceImpl");
		return listaTipoDietaDTO;
	}

	/* leggi Tipo Dieta per idMensa */
	@Override
	public List<TipoDietaDTO> getTipoDietaPerMensa(int idMensa) 
	{
		logger.info("Accesso a getTipoDietaPerMensa classe MensaServiceImpl");
		List<TipoDieta> listaTipoDieta = mensaDAO.getTipoDietaPerMensa(idMensa);
		List<TipoDietaDTO> listaTipoDietaDTO = new ArrayList<>();
		logger.info("Inizio ciclo for in getTipoDietaPerMensa classe MensaServiceImpl");
		for(TipoDieta td: listaTipoDieta)
		{
			listaTipoDietaDTO.add(TipoDietaMapper.mapToDTO(td));
		}
		logger.info("Fine getAllTipoDieta classe MensaServiceImpl");
		return listaTipoDietaDTO;
	}


	@Override
	public FEMensaCompletaDTO getSingolaMensaCompleta(int idMensa) 
	{
		FEMensaCompletaDTO feMensaCompletaDTO = new FEMensaCompletaDTO();

		logger.info("Accesso a getSingolaMensa classe MensaServiceImpl");
		//Servizi Evento
		List<ServizioEvento> listaServizioEvento = mensaDAO.getServizioEventoPerMensa(idMensa);
		List<ServizioEventoDTO> listaServizioEventoDTO = new ArrayList<>();
		for(ServizioEvento se : listaServizioEvento)
		{
			listaServizioEventoDTO.add(ServizioEventoMapper.mapToDTO(se, dateFormat));
		}
		feMensaCompletaDTO.setListaServizioEventoDTO(listaServizioEventoDTO);

		//Tipo Dieta
		List<TipoDieta> listaTipoDieta = mensaDAO.getTipoDietaPerMensa(idMensa);
		List<TipoDietaDTO> listaTipoDietaDTO = new ArrayList<>();
		feMensaCompletaDTO.setListaTipoDietaDTO(listaTipoDietaDTO);
		for(TipoDieta td: listaTipoDieta)
		{
			listaTipoDietaDTO.add(TipoDietaMapper.mapToDTO(td));
		}
		//Tipo Pasto
		FEServizioMensaDTO feServizioMensaDTO = new FEServizioMensaDTO();
		List<AssTipoPastoMensa> listaAssTipoPastoMensa = mensaDAO.getServiziPerMensa(idMensa);

		if(listaAssTipoPastoMensa.size()>0)
		{
			ModelMapper mapper = new ModelMapper();
			List<TipoPastoDTO> listaTipoPastoDTO = new ArrayList<>();
			Mensa mensa = null;
			DateTimeFormatter dateTime = DateTimeFormatter.ofPattern("HH:mm");

			for(AssTipoPastoMensa ass : listaAssTipoPastoMensa)
			{
				if(mensa == null)
					mensa = ass.getMensa();

				TipoPastoDTO tipoPastoDTO = (mapper.map(ass.getTipoPasto(), TipoPastoDTO.class));
				tipoPastoDTO.setOraFinePrenotazione(ass.getOraFinePrenotazione() != null ? ass.getOraFinePrenotazione().format(dateTime) : null);
				tipoPastoDTO.setOrarioAl(ass.getOrarioAl() != null ? ass.getOrarioAl().format(dateTime) : null);
				tipoPastoDTO.setOrarioDal(ass.getOrarioDal() != null ? ass.getOrarioDal().format(dateTime) : null);
				listaTipoPastoDTO.add(tipoPastoDTO);
			}

			feServizioMensaDTO.setListaTipoPastoDTO(listaTipoPastoDTO);
			SimpleDateFormat formatter = new SimpleDateFormat(this.dateFormat);

			feServizioMensaDTO.setDataFineServizio(mensa.getDataFineServizio() != null ? formatter.format(mensa.getDataFineServizio()) : null);
			feServizioMensaDTO.setDataInizioServizio(mensa.getDataInizioServizio() != null ? formatter.format(mensa.getDataInizioServizio()) : null);	

			feMensaCompletaDTO.setListaTipoPastoDTO(listaTipoPastoDTO);
		}

		//Tipo Locali
		List<FELocaliDTO> listaFeLocaliDTO = new ArrayList<>();	
		String query = "select	tipo_locale.descrizione_tipo_locale,\r\n"
				+ "		ass_mensa_tipo_locale.superficie,\r\n"
				+ "		ass_mensa_tipo_locale.numero_locali,\r\n"
				+ "		ass_mensa_tipo_locale.note,\r\n"
				+ "		tipo_locale.codice_tipo_locale\r\n"
				+ "from	tipo_locale INNER join ass_mensa_tipo_locale\r\n"
				+ "on	codice_mensa_fk = " + idMensa + " and \r\n"
				+ "	tipo_locale.codice_tipo_locale = ass_mensa_tipo_locale.codice_tipo_locale_fk;";

		logger.info("Esecuzione query: " + query); 
		Query sumQuery = entityManager.createNativeQuery(query);
		List<Object[]> listOfResults = sumQuery.getResultList();

		for(Object[] ob : listOfResults)
		{
			FELocaliDTO feLocaliDTO = new FELocaliDTO();
			feLocaliDTO.setNomeLocale((String) ob[0]);
			Integer superfice = (Integer) ob[1];
			feLocaliDTO.setSuperfice(superfice);
			Integer numeroLocali = (Integer) ob[2];
			feLocaliDTO.setNumeroLocali(numeroLocali);;
			feLocaliDTO.setNote((String) ob[3]);
			feLocaliDTO.setCodiceTipoLocale((Integer) ob[4]);
			listaFeLocaliDTO.add(feLocaliDTO);
		}

		feMensaCompletaDTO.setListaTipoLocaleDTO(listaFeLocaliDTO);

		//Mensa
		Mensa mensa = mensaDAO.getSingolaMensa(idMensa);
		MensaDTO mensaDTO = MensaMapper.mapToDTO(mensa, dateFormat);

		//Mensa base
		if(mensaDTO.getCodiceMensa() != null)
			feMensaCompletaDTO.setCodiceMensa(mensaDTO.getCodiceMensa());	
		if(!StringUtils.isBlank(mensaDTO.getDescrizioneMensa()))
			feMensaCompletaDTO.setDescrizioneMensa(mensaDTO.getDescrizioneMensa());
		if(!StringUtils.isBlank(mensaDTO.getNumeroAutorizzazioneSanitaria()))
			feMensaCompletaDTO.setNumeroAutorizzazioneSanitaria(mensaDTO.getNumeroAutorizzazioneSanitaria());
		if(!StringUtils.isBlank(mensaDTO.getDataAutorizzazioneSanitaria()))	
			feMensaCompletaDTO.setDataAutorizzazioneSanitaria(mensaDTO.getDataAutorizzazioneSanitaria());
		if(!StringUtils.isBlank(mensaDTO.getAutSanitariaRilasciataDa()))
			feMensaCompletaDTO.setAutSanitariaRilasciataDa(mensaDTO.getAutSanitariaRilasciataDa());
		if(!StringUtils.isBlank(mensaDTO.getDataInizioServizio()))
			feMensaCompletaDTO.setDataInizioServizio(mensaDTO.getDataInizioServizio());
		if(!StringUtils.isBlank(mensaDTO.getDataFineServizio()))
			feMensaCompletaDTO.setDataFineServizio(mensaDTO.getDataFineServizio());
		if(!StringUtils.isBlank(mensaDTO.getDescrizioneTipoFormaVettovagliamento()))
			feMensaCompletaDTO.setDescrizioneTipoFormaVettovagliamento(mensaDTO.getDescrizioneTipoFormaVettovagliamento());
		if(mensaDTO.getCodiceTipoFormaVettovagliamento() != null)
			feMensaCompletaDTO.setCodiceTipoFormaVettovagliamento(mensaDTO.getCodiceTipoFormaVettovagliamento());
		if(!StringUtils.isBlank(mensaDTO.getTipoDieta()))
			feMensaCompletaDTO.setTipoDieta(mensaDTO.getTipoDieta());

		//Contatti
		if(!StringUtils.isBlank(mensaDTO.getTipoDieta()))
			feMensaCompletaDTO.setVia(mensaDTO.getVia());
		if(mensaDTO.getNumeroCivico() != null)
			feMensaCompletaDTO.setNumeroCivico(mensaDTO.getNumeroCivico());
		if(!StringUtils.isBlank(mensaDTO.getCap()))
			feMensaCompletaDTO.setCap(mensaDTO.getCap());
		if(!StringUtils.isBlank(mensaDTO.getCitta()))
			feMensaCompletaDTO.setCitta(mensaDTO.getCitta());
		if(!StringUtils.isBlank(mensaDTO.getProvincia()))
			feMensaCompletaDTO.setProvincia(mensaDTO.getProvincia());
		if(!StringUtils.isBlank(mensaDTO.getTelefono()))
			feMensaCompletaDTO.setTelefono(mensaDTO.getTelefono());
		if(!StringUtils.isBlank(mensaDTO.getFax()))
			feMensaCompletaDTO.setFax(mensaDTO.getFax());
		if(!StringUtils.isBlank(mensaDTO.getEmail()))
			feMensaCompletaDTO.setEmail(mensaDTO.getEmail());


		//Servizi Festivi
//		if(!StringUtils.isBlank(mensaDTO.getServizioFestivo()))
//			feMensaCompletaDTO.setServizioFestivo(mensaDTO.getServizioFestivo());
		if(!StringUtils.isBlank(mensaDTO.getServizioFestivoSabato()))
			feMensaCompletaDTO.setServizioFestivoSabato(mensaDTO.getServizioFestivoSabato());
		if(!StringUtils.isBlank(mensaDTO.getServizioFestivoDomenica()))
			feMensaCompletaDTO.setServizioFestivoDomenica(mensaDTO.getServizioFestivoDomenica());

		//Controlli Aggiuntivi
		feMensaCompletaDTO.setPresenzaFile(mensaDTO.isPresenzaFile());
		if(!StringUtils.isBlank(mensaDTO.getDescrizioneEnte()))
			feMensaCompletaDTO.setDescrizioneEnte(mensaDTO.getDescrizioneEnte());
		if(mensaDTO.getIdEnte() != 0)
			feMensaCompletaDTO.setIdEnte(mensaDTO.getIdEnte());

		logger.info("Fine getSingolaMensa classe MensaServiceImpl");
		return feMensaCompletaDTO;
	}


	@Override
	public List<MensaDTO> ricercaMense(int idEnte, List<RicercaColonnaDTO> colonne) 
	{
		logger.info("Accesso a ricercaMense, classe MensaServiceImpl");
		List<Mensa> listaMensa = mensaDAO.ricercaMense(idEnte, colonne);
		List<MensaDTO> listaMensaDTO = new ArrayList<>();
		logger.info("Inizio ciclo For in getAllMense, classe MensaServiceImpl");
		for(Mensa m : listaMensa)
		{
			MensaDTO mensaDTO = null;
			mensaDTO = MensaMapper.mapToDTO(m, dateFormat);
			if(m.getEnte() != null)
				mensaDTO.setDescrizioneEnte(m.getEnte().getDescrizioneEnte());
			listaMensaDTO.add(mensaDTO);
		}
		logger.info("Fine getAllMense, classe MensaServiceImpl");
		return listaMensaDTO;
	}

}
