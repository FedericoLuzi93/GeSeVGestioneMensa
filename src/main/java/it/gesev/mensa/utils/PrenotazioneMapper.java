package it.gesev.mensa.utils;

import java.text.SimpleDateFormat;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import it.gesev.mensa.dto.PastiPrenotatiDTO;
import it.gesev.mensa.entity.Prenotazione;

public class PrenotazioneMapper 
{
	private static final Logger logger = LoggerFactory.getLogger(PrenotazioneMapper.class);

	//Entity to DTO
	public static PastiPrenotatiDTO mapToDTO(Prenotazione prenotazione, String dateFormat)
	{
		logger.info("Accesso a mapToDTO, classe PastiConsumatiMapper");
		ModelMapper mapper = new ModelMapper();
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateFormat);
		
		PastiPrenotatiDTO pDTO = mapper.map(prenotazione, PastiPrenotatiDTO.class);
		pDTO.setDataPrenotazione(simpleDateFormat.format(prenotazione.getDataPrenotazione()));
		return pDTO;
	}

	//DTO to Entity
	public static Prenotazione mapToEntity(PastiPrenotatiDTO prenotazioneDTO)
	{
		logger.info("Accesso a mapToEntity, classe PastiConsumatiMapper");
		ModelMapper mapper = new ModelMapper();
		Prenotazione p = mapper.map(prenotazioneDTO, Prenotazione.class);
		return p;
	}
}
