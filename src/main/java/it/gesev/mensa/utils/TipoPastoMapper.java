package it.gesev.mensa.utils;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import it.gesev.mensa.dto.AssMensaTipoLocaleDTO;
import it.gesev.mensa.dto.AssTipoPastoMensaDTO;
import it.gesev.mensa.dto.CreaMensaDTO;
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

	
	//Integer List from CreaMensaDTO
	public static List<Integer> mapToEntityFromMensa(CreaMensaDTO creaMensaDTO)
	{
//		logger.info("Accesso a mapToEntity classe mapToEntityFromMensa");
//		ModelMapper mapper = new ModelMapper();
//		List<TipoPasto> listaTipoPasto = new ArrayList<>();
//		List<AssTipoPastoMensaDTO> listaAssociativa = creaMensaDTO.getAssTipoPastoMensaDTO();
//		List<Integer> listaCodiciPasto = new ArrayList<>();
//		int numero = 0;
//		for(AssTipoPastoMensaDTO associativa : listaAssociativa)
//		{
//			numero = associativa.getTipoPastoDTO().getCodiceTipoPasto();
//			listaCodiciPasto.add(numero);
//		}
//		return listaCodiciPasto;
		return null;
		
	}

}
