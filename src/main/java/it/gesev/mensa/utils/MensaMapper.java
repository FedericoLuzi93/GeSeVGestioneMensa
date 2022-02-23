package it.gesev.mensa.utils;

import java.text.SimpleDateFormat;

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
		if(mensa.getDataAutorizzazioneSanitaria() != null)
			mensaDTO.setDataAutorizzazioneSanitaria(simpleDateFormat.format(mensa.getDataAutorizzazioneSanitaria()));
		return mensaDTO;
	}
	
	//DTO to Entity
	public static Mensa mapToEntity(MensaDTO mensaDTO)
	{
		logger.info("Accesso a mapToEntity, classe MensaMapper");
		ModelMapper mapper = new ModelMapper();
		Mensa mensa= mapper.map(mensaDTO, Mensa.class);
		return mensa;
	}
}
