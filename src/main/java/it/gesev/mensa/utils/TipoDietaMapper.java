package it.gesev.mensa.utils;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import it.gesev.mensa.dto.TipoDietaDTO;
import it.gesev.mensa.entity.TipoDieta;

public class TipoDietaMapper 
{
	private static final Logger logger = LoggerFactory.getLogger(TipoDietaMapper.class);
	
	//Entity to DTO
	public static TipoDietaDTO mapToDTO(TipoDieta tipoDieta)
	{
		logger.info("Accesso a mapToDTO, classe TipoDietaMapper");
		ModelMapper mapper = new ModelMapper();
		TipoDietaDTO tipoDietaDTO = mapper.map(tipoDieta, TipoDietaDTO.class);
		return tipoDietaDTO;
	}
	
	//DTO to Entity
	public static TipoDieta mapToEntity(TipoDietaDTO tipoDietaDTO)
	{
		logger.info("Accesso a mapToDTO, classe TipoDietaMapper");
		
		ModelMapper mapper = new ModelMapper();
		TipoDieta tipoDieta = mapper.map(tipoDietaDTO, TipoDieta.class);
		return tipoDieta;
	}
}
