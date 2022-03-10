package it.gesev.mensa.utils;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import it.gesev.mensa.dto.OrganoDirettivoDTO;
import it.gesev.mensa.entity.OrganoDirettivo;

public class OrganoDirettivoMapper
{
private static final Logger logger = LoggerFactory.getLogger(OrganoDirettivoMapper.class);
	
	//Entity to DTO
	public static OrganoDirettivoDTO mapToDTO(OrganoDirettivo organoDirettivo)
	{
		logger.info("Accesso a mapToDTO classe OrganoDirettivoMapper");
		ModelMapper mapper = new ModelMapper();
		OrganoDirettivoDTO organoDirettivoDTO = mapper.map(organoDirettivo, OrganoDirettivoDTO.class);
		return organoDirettivoDTO;
	}
	
	//DTO to Entity
	public static OrganoDirettivo mapToEntity(OrganoDirettivoDTO organoDirettivoDTO)
	{
		logger.info("Accesso a mapToEntity classe OrganoDirettivoMapper");
		ModelMapper mapper = new ModelMapper();
		OrganoDirettivo organoDirettivo = mapper.map(organoDirettivoDTO, OrganoDirettivo.class);
		return organoDirettivo;
	}

}
