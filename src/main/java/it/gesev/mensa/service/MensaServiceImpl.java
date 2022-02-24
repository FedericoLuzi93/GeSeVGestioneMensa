package it.gesev.mensa.service;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import it.gesev.mensa.dao.MensaDAO;
import it.gesev.mensa.dto.MensaDTO;
import it.gesev.mensa.entity.Mensa;
import it.gesev.mensa.exc.GesevException;
import it.gesev.mensa.utils.MensaMapper;

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
	public int createMensa(MensaDTO mensaDTO) 
	{
		Mensa mensa = null;
		try
		{
			logger.info("Accesso a createMensa, classe MensaServiceImpl");
 			mensa = MensaMapper.mapToEntity(mensaDTO);			
		}
		catch(GesevException exc)
		{
			logger.info("Eccezione nel servizio createMensa" + exc);
			throw new GesevException("Non è stato possibile creare la Mensa " + exc, HttpStatus.BAD_REQUEST);
		}
		logger.info("Crezione mensa in corso...");
		return mensaDAO.createMensa(mensa);
	}

	/* Aggiorna una Mensa */
	public int updateMensa(MensaDTO mensaDTO, int idMensa) 
	{
		Mensa mensa = null;
		try
		{
			logger.info("Accesso a updateMensa, classe MensaServiceImpl");
			mensa = MensaMapper.mapToEntity(mensaDTO);			
		}
		catch(GesevException exc)
		{
			logger.info("Eccezione nel servizio updateMensa" + exc);
			throw new GesevException("Non è stato possibile aggiornare la mensa" + exc, HttpStatus.BAD_REQUEST);
		}
		logger.info("Aggiornamento in corso...");
		mensaDAO.updateMensa(mensa, idMensa);
		return mensa.getCodiceMensa();
	}

}
