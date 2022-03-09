package it.gesev.mensa.utils;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import it.gesev.mensa.dto.TipoPastoDTO;
import it.gesev.mensa.entity.TipoPasto;

public class TipoPastoMapper 
{
private static final Logger logger = LoggerFactory.getLogger(TipoPastoMapper.class);
	
	//Entity to DTO
	public static TipoPastoDTO mapToDTO(TipoPasto tipoPasto)
	{
		logger.info("Accesso a mapToDTO, classe TipoPastoMapper");
		ModelMapper mapper = new ModelMapper();
		TipoPastoDTO tipoPastoDTO = mapper.map(tipoPasto, TipoPastoDTO.class);
		return tipoPastoDTO;
	}
	
	//DTO to Entity
	public static TipoPasto mapToEntity(TipoPastoDTO tipoPastoDTO)
	{
		logger.info("Accesso a mapToDTO, classe TipoPastoMapper");
		ModelMapper mapper = new ModelMapper();
		TipoPasto tipoPasto = mapper.map(tipoPastoDTO, TipoPasto.class);
		return tipoPasto;
	}


}
