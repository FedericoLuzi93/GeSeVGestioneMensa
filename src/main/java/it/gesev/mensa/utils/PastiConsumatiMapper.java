package it.gesev.mensa.utils;
import java.text.SimpleDateFormat;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import it.gesev.mensa.dto.PastiConsumatiDTO;
import it.gesev.mensa.entity.PastiConsumati;


public class PastiConsumatiMapper 
{

	private static final Logger logger = LoggerFactory.getLogger(PastiConsumatiMapper.class);

	//Entity to DTO
	public static PastiConsumatiDTO mapToDTO(PastiConsumati pastiConsumati, String dateFormat)
	{
		logger.info("Accesso a mapToDTO, classe PastiConsumatiMapper");
		ModelMapper mapper = new ModelMapper();
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateFormat);
		
		PastiConsumatiDTO pcDTO = mapper.map(pastiConsumati, PastiConsumatiDTO.class);
		pcDTO.setDataPasto(simpleDateFormat.format(pastiConsumati.getDataPasto()));
		
		return pcDTO;
	}

	//DTO to Entity
	public static PastiConsumati mapToEntity(PastiConsumatiDTO pastiConsumatiDTO)
	{
		logger.info("Accesso a mapToEntity, classe PastiConsumatiMapper");
		ModelMapper mapper = new ModelMapper();
		PastiConsumati pc = mapper.map(pastiConsumatiDTO, PastiConsumati.class);
		return pc;
	}

}
