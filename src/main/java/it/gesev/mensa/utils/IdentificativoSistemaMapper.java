package it.gesev.mensa.utils;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import it.gesev.mensa.dto.EnteDTO;
import it.gesev.mensa.dto.IdentificativoSistemaDTO;
import it.gesev.mensa.entity.Ente;
import it.gesev.mensa.entity.IdentificativoSistema;

public class IdentificativoSistemaMapper 
{
	private static final Logger logger = LoggerFactory.getLogger(IdentificativoSistemaMapper.class);
	
	//Entity to DTO
	public static IdentificativoSistemaDTO mapToDTO(IdentificativoSistema identificativoSistema)
	{
		logger.info("Accesso a mapToDTO, classe IdentificativoSistemaMapper");
		ModelMapper mapper = new ModelMapper();
		IdentificativoSistemaDTO isDTO = mapper.map(identificativoSistema, IdentificativoSistemaDTO.class);
		return isDTO;
	}
	
	//DTO to Entity
	public static IdentificativoSistema mapToEntity(IdentificativoSistemaDTO identificativoSistemaDTO)
	{
		logger.info("Accesso a mapToEntity, classe IdentificativoSistemaMapper");
		ModelMapper mapper = new ModelMapper();
		IdentificativoSistema is = mapper.map(identificativoSistemaDTO, IdentificativoSistema.class);
		return is;
	}
}
