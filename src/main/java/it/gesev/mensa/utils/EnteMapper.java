package it.gesev.mensa.utils;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import it.gesev.mensa.dto.EnteDTO;
import it.gesev.mensa.entity.Ente;

public class EnteMapper 
{
	private static final Logger logger = LoggerFactory.getLogger(MensaMapper.class);

	//Entity to DTO
	public static EnteDTO mapToDTO(Ente ente)
	{
		logger.info("Accesso a mapToDTO, classe EnteMapper");
		ModelMapper mapper = new ModelMapper();
		EnteDTO enteDTO = mapper.map(ente, EnteDTO.class);	
		return enteDTO;
	}
	
	//DTO to Entity
	public static Ente mapToEntity(EnteDTO enteDTO)
	{
		logger.info("Accesso a mapToEntity, classe EnteMapper");
		ModelMapper mapper = new ModelMapper();
		Ente ente= mapper.map(enteDTO, Ente.class);
		return ente;
	}
}


