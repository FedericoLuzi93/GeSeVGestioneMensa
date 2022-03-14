package it.gesev.mensa.utils;

import java.text.SimpleDateFormat;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import it.gesev.mensa.dto.ServizioEventoDTO;
import it.gesev.mensa.entity.ServizioEvento;

public class ServizioEventoMapper 
{
	private static final Logger logger = LoggerFactory.getLogger(TipoLocaleMapper.class);
	
	//Entity to DTO
	public static ServizioEventoDTO mapToDTO(ServizioEvento servizioEvento, String dateFormat)
	{
		logger.info("Accesso a mapToDTO, classe ServizioEventoMapper");
		ModelMapper mapper = new ModelMapper();
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateFormat);
		
		ServizioEventoDTO eventoDTO = mapper.map(servizioEvento, ServizioEventoDTO.class);
		
		if(servizioEvento.getDataServizioEvento() != null)
			eventoDTO.setDataServizioEvento(simpleDateFormat.format(servizioEvento.getDataServizioEvento()));
		return eventoDTO;
	}
	
	//DTO to Entity
	public static ServizioEvento mapToEntity(ServizioEventoDTO eventoDTO)
	{
		logger.info("Accesso a mapToDTO, classe ServizioEventoMapper");
		
		ModelMapper mapper = new ModelMapper();
		ServizioEvento evento = mapper.map(eventoDTO, ServizioEvento.class);
		return evento;
	}
}
