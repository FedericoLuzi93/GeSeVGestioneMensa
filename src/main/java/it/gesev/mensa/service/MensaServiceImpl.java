package it.gesev.mensa.service;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.commons.lang3.StringUtils;
import org.jvnet.staxex.MtomEnabled;
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
import it.gesev.mensa.dto.FileDTO;
import it.gesev.mensa.dto.MensaDTO;
import it.gesev.mensa.dto.TipoFromaVettovagliamentoDTO;
import it.gesev.mensa.dto.TipoLocaleDTO;
import it.gesev.mensa.entity.AssMensaTipoLocale;
import it.gesev.mensa.entity.Ente;
import it.gesev.mensa.entity.Mensa;
import it.gesev.mensa.entity.TipoFormaVettovagliamento;
import it.gesev.mensa.entity.TipoLocale;
import it.gesev.mensa.exc.GesevException;
import it.gesev.mensa.utils.AssMensaTipoLocaleMapper;
import it.gesev.mensa.utils.EnteMapper;
import it.gesev.mensa.utils.MensaMapper;
import it.gesev.mensa.utils.TipoFormaVettovagliamentoMapper;
import it.gesev.mensa.utils.TipoLocaleMapper;

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
			listaMensaDTO.add(MensaMapper.mapToDTO(m, dateFormat));
		}
		logger.info("Fine getAllMense, classe MensaServiceImpl");
		return listaMensaDTO;
	}

	/* Crea una Mensa */
	public int createMensa(CreaMensaDTO creaMensaDTO, MultipartFile multipartFile) throws ParseException, IOException 
	{
		Mensa mensa = null;
		List<AssMensaTipoLocale> assMensaTipoLocale = null;
		String descrizioneTipoVettovagliamento = "";
		try
		{
			logger.info("Accesso a createMensa, classe MensaServiceImpl");
 			mensa = MensaMapper.mapToEntity(creaMensaDTO, dateFormat);	
 			assMensaTipoLocale = AssMensaTipoLocaleMapper.mapToEntity(creaMensaDTO, dateFormat);
 			descrizioneTipoVettovagliamento = creaMensaDTO.getDescrizioneTipoFormaVettovagliamento();
 			if(multipartFile == null)
 				mensa.setAutorizzazioneSanitaria(null);
 			else
 				mensa.setAutorizzazioneSanitaria(multipartFile.getBytes());	
		}
		catch(GesevException exc)
		{
			logger.info("Eccezione nel servizio createMensa" + exc);
			throw new GesevException("Non è stato possibile creare la Mensa " + exc, HttpStatus.BAD_REQUEST);
		}
		logger.info("Crezione mensa in corso...");
		
		return mensaDAO.createMensa(mensa, assMensaTipoLocale, descrizioneTipoVettovagliamento);
	}

	/* Aggiorna una Mensa */
	public int updateMensa(CreaMensaDTO creaMensaDTO, int idMensa, MultipartFile multipartFile) throws ParseException, IOException 
	{
		Mensa mensa = null;
		List<AssMensaTipoLocale> assMensaTipoLocale = null;
		String descrizioneTipoVettovagliamento = "";
		try
		{
			logger.info("Accesso a updateMensa, classe MensaServiceImpl");
 			mensa = MensaMapper.mapToEntity(creaMensaDTO, dateFormat);	
 			descrizioneTipoVettovagliamento = creaMensaDTO.getDescrizioneTipoFormaVettovagliamento();
 			if(multipartFile != null)
 				mensa.setAutorizzazioneSanitaria(multipartFile.getBytes());
 			else
 				mensa.setAutorizzazioneSanitaria(null);
 			assMensaTipoLocale = AssMensaTipoLocaleMapper.mapToEntity(creaMensaDTO, dateFormat);
 			
		}
		catch(GesevException exc)
		{
			logger.info("Eccezione nel servizio updateMensa" + exc);
			throw new GesevException("Non è stato possibile modificare la Mensa " + exc, HttpStatus.BAD_REQUEST);
		}
		logger.info("Modifica mensa in corso...");
		return mensaDAO.updateMensa(mensa, assMensaTipoLocale, idMensa, descrizioneTipoVettovagliamento);
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
		catch(GesevException exc)
		{
			logger.info("Eccezione nel servizio disableMensa" + exc);
			throw new GesevException("Non è stato possibile disabilitare la Mensa " + exc, HttpStatus.BAD_REQUEST);
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
				+ "		ass_mensa_tipo_locale.note\r\n"
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
			feLocaliDTO.setNumero(numeroLocali);;
			feLocaliDTO.setNote((String) ob[3]);
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


}
