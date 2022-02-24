package it.gesev.mensa.utils;

import java.text.SimpleDateFormat;
import java.time.LocalTime;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import it.gesev.mensa.dto.MensaDTO;
import it.gesev.mensa.entity.Mensa;

public class MensaMapper 
{
	private static final Logger logger = LoggerFactory.getLogger(MensaMapper.class);

	//Entity to DTO
	public static MensaDTO mapToDTO(Mensa mensa, String dateFormat)
	{
		logger.info("Accesso a mapToDTO, classe MensaMapper");
		ModelMapper mapper = new ModelMapper();
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateFormat);
		MensaDTO mensaDTO= mapper.map(mensa, MensaDTO.class);
		
		//Date to String
		if(mensa.getDataAutorizzazioneSanitaria() != null)
			mensaDTO.setDataAutorizzazioneSanitaria(simpleDateFormat.format(mensa.getDataAutorizzazioneSanitaria()));
		
		//LocalTime a String
		mensaDTO.setOrarioAl(mensa.getOrarioAl().toString());
		mensaDTO.setOrarioDal(mensa.getOrarioDal().toString());
		mensaDTO.setOraFinePrenotazione(mensa.getOraFinePrenotazione().toString());
		
		return mensaDTO;
	}
	
	//DTO to Entity
	public static Mensa mapToEntity(MensaDTO mensaDTO)
	{
		logger.info("Accesso a mapToEntity, classe MensaMapper");
		ModelMapper mapper = new ModelMapper();
		Mensa mensa= mapper.map(mensaDTO, Mensa.class);
		
		
			
		//String to LocalTime
		mensa.setOrarioAl(ControlloData.controlloTempo(mensaDTO.getOrarioAl()));
		mensa.setOrarioDal(ControlloData.controlloTempo(mensaDTO.getOrarioDal()));
		mensa.setOraFinePrenotazione(ControlloData.controlloTempo(mensaDTO.getOraFinePrenotazione()));
		
		return mensa;
	}
}
