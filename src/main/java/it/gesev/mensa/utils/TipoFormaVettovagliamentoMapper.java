package it.gesev.mensa.utils;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import it.gesev.mensa.dto.CreaMensaDTO;
import it.gesev.mensa.dto.TipoFromaVettovagliamentoDTO;
import it.gesev.mensa.dto.TipoLocaleDTO;
import it.gesev.mensa.entity.TipoFormaVettovagliamento;
import it.gesev.mensa.entity.TipoLocale;

public class TipoFormaVettovagliamentoMapper 
{
	private static final Logger logger = LoggerFactory.getLogger(TipoFormaVettovagliamentoMapper.class);
	
	//Entity to DTO
	public static TipoFromaVettovagliamentoDTO mapToDTO(TipoFormaVettovagliamento tipoFormaVettovagliamento)
	{
		logger.info("Accesso a mapToDTO, classe TipoFormaVettovagliamentoMapper");
		ModelMapper mapper = new ModelMapper();
		TipoFromaVettovagliamentoDTO vettovagliamentoDTO = mapper.map(tipoFormaVettovagliamento, TipoFromaVettovagliamentoDTO.class);
		return vettovagliamentoDTO;
	}
	
	//DTO to Entity
	public static TipoFormaVettovagliamento mapToEntity(TipoFromaVettovagliamentoDTO tipoFromaVettovagliamentoDTO)
	{
		logger.info("Accesso a mapToDTO, classe TipoLocaleMapper");
		
		ModelMapper mapper = new ModelMapper();
		TipoFormaVettovagliamento vettovagliamento = mapper.map(tipoFromaVettovagliamentoDTO, TipoFormaVettovagliamento.class);
		return vettovagliamento;
	}

}
