package it.gesev.mensa.utils;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import it.gesev.mensa.dto.CreaMensaDTO;
import it.gesev.mensa.dto.TipoLocaleDTO;
import it.gesev.mensa.entity.TipoLocale;

public class TipoLocaleMapper 
{
	private static final Logger logger = LoggerFactory.getLogger(MensaMapper.class);
	
	//Entity to DTO
	public static TipoLocaleDTO mapToDTO(TipoLocale tipoLocale)
	{
		logger.info("Accesso a mapToDTO, classe TipoLocaleMapper");
		ModelMapper mapper = new ModelMapper();
		TipoLocaleDTO tipoLocaleDTO = mapper.map(tipoLocale, TipoLocaleDTO.class);
		return tipoLocaleDTO;
	}
	
	//DTO to Entity
	public static TipoLocale mapToEntity(CreaMensaDTO creaMensaDTO)
	{
		logger.info("Accesso a mapToDTO, classe TipoLocaleMapper");
		
		ModelMapper mapper = new ModelMapper();
		TipoLocale tipoLocale = mapper.map(creaMensaDTO, TipoLocale.class);
		return tipoLocale;
	}

}
