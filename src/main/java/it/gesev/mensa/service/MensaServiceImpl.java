package it.gesev.mensa.service;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
import it.gesev.mensa.dto.TipoLocaleDTO;
import it.gesev.mensa.entity.AssMensaTipoLocale;
import it.gesev.mensa.entity.Ente;
import it.gesev.mensa.entity.Mensa;
import it.gesev.mensa.entity.TipoLocale;
import it.gesev.mensa.exc.GesevException;
import it.gesev.mensa.utils.AssMensaTipoLocaleMapper;
import it.gesev.mensa.utils.EnteMapper;
import it.gesev.mensa.utils.MensaMapper;
import it.gesev.mensa.utils.TipoLocaleMapper;

@Service
public class MensaServiceImpl implements MensaService 
{
	@Autowired
	private MensaDAO mensaDAO;
	
	@Value("${gesev.data.format}")
	private String dateFormat;
		
	private static final Logger logger = LoggerFactory.getLogger(MensaServiceImpl.class);

	/* Leggi tutte le Mense */
	public List<MensaDTO> getAllMense() 
	{
		logger.info("Accesso a getAllMense, classe MensaServiceImpl");
		List<Mensa> listaMensa = mensaDAO.getAllMense();
		List<MensaDTO> listaMensaDTO = new ArrayList<>();
		for(Mensa m : listaMensa)
		{
			logger.info("Ciclo For in getAllMense, classe MensaServiceImpl");
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
		try
		{
			logger.info("Accesso a createMensa, classe MensaServiceImpl");
 			mensa = MensaMapper.mapToEntity(creaMensaDTO, dateFormat);	
 			assMensaTipoLocale = AssMensaTipoLocaleMapper.mapToEntity(creaMensaDTO, dateFormat);
 			mensa.setAutorizzazioneSanitaria(multipartFile.getBytes());	
		}
		catch(GesevException exc)
		{
			logger.info("Eccezione nel servizio createMensa" + exc);
			throw new GesevException("Non è stato possibile creare la Mensa " + exc, HttpStatus.BAD_REQUEST);
		}
		logger.info("Crezione mensa in corso...");
		
		return mensaDAO.createMensa(mensa, assMensaTipoLocale);
	}

	/* Aggiorna una Mensa */
	public int updateMensa(CreaMensaDTO creaMensaDTO, int idMensa, MultipartFile multipartFile) throws ParseException, IOException 
	{
		Mensa mensa = null;
		List<AssMensaTipoLocale> assMensaTipoLocale = null;
		try
		{
			logger.info("Accesso a updateMensa, classe MensaServiceImpl");
 			mensa = MensaMapper.mapToEntity(creaMensaDTO, dateFormat);	
 			if(multipartFile != null)
 				mensa.setAutorizzazioneSanitaria(multipartFile.getBytes());
 			assMensaTipoLocale = AssMensaTipoLocaleMapper.mapToEntity(creaMensaDTO, dateFormat);
 			
		}
		catch(GesevException exc)
		{
			logger.info("Eccezione nel servizio updateMensa" + exc);
			throw new GesevException("Non è stato possibile modificare la Mensa " + exc, HttpStatus.BAD_REQUEST);
		}
		logger.info("Modifica mensa in corso...");
		return mensaDAO.updateMensa(mensa, assMensaTipoLocale, idMensa);
	}

	/* Disabilita una Mensa */
	@Override
	public int disableMensa(MensaDTO mensaDTO, int idMensa) throws ParseException 
	{
		Mensa mensa = null;
		try
		{
			String dataString = mensaDTO.getDataAutorizzazioneSanitaria();
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateFormat);
			Date date = simpleDateFormat.parse(dataString);
			mensa.setDataAutorizzazioneSanitaria(date);	 			
		}
		catch(GesevException exc)
		{
			logger.info("Eccezione nel servizio disableMensa" + exc);
			throw new GesevException("Non è stato possibile disabilitare la Mensa " + exc, HttpStatus.BAD_REQUEST);
		}
		logger.info("disabilitazione mensa in corso...");
		return mensaDAO.disableMensa(mensa, idMensa);
	}
	
	/* Invio del File */
	@Override
	public FileDTO getFile(int idMensa) 
	{
		Mensa mensa = mensaDAO.getFile(idMensa);
		FileDTO fileDTO = new FileDTO();
		SimpleDateFormat date = new SimpleDateFormat(dateFormat);
		fileDTO.setNomeFile(mensa.getDescrizioneMensa() + "_" + date.format(mensa.getDataAutorizzazioneSanitaria() + ".pdf")); 
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
		for(TipoLocale tp : listaTipoLocali)
		{
			logger.info("Ciclo For in getAllLocali, classe MensaServiceImpl");
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
		List<AssMensaTipoLocale> listaAssMensaTipoLocale = mensaDAO.getAssMensaTipoLocaleByMensa(idMensa);
		List<TipoLocale> listaTipoLocale = mensaDAO.getAllLocali();
		List<FELocaliDTO> listaFeLocaliDTO = new ArrayList<>();
		FELocaliDTO feLocaliDTO = new FELocaliDTO();
		
		//For each dove ciclo gli array nella lista di obj ottenuta dalla query nativa
		//Integer chiusi = new Integer(((BigDecimal)row[5]).intValue());
		for(AssMensaTipoLocale as : listaAssMensaTipoLocale)
		{
			for(TipoLocale tl : listaTipoLocale)
			{
				if(tl.getCodiceTipoLocale() == as.getTipoLocale().getCodiceTipoLocale())
				{
					feLocaliDTO.setNomeLocale(tl.getDescrizioneTipoLocale());
					feLocaliDTO.setNumero(as.getNumeroLocali());
					feLocaliDTO.setSuperfice(as.getSuperficie());
					listaFeLocaliDTO.add(feLocaliDTO);
				}
			}
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
		for(Ente e : enteLista)
		{
			logger.info("Ciclo For in getAllEnti, classe MensaServiceImpl");
			listaEnteDTO.add(EnteMapper.mapToDTO(e));
		}
		logger.info("Fine getAllEnti, classe MensaServiceImpl");
		return listaEnteDTO;
	}
}
