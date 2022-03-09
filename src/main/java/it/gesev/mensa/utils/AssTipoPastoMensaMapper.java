package it.gesev.mensa.utils;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import it.gesev.mensa.dto.AssTipoPastoMensaDTO;
import it.gesev.mensa.dto.CreaMensaDTO;
import it.gesev.mensa.entity.AssTipoPastoMensa;
import it.gesev.mensa.entity.TipoPasto;

public class AssTipoPastoMensaMapper 
{
	private static final Logger logger = LoggerFactory.getLogger(AssTipoPastoMensaMapper.class);

	//Entity to DTO
	public static AssTipoPastoMensaDTO mapToDTO(AssTipoPastoMensa assTipoPastoMensa, String dateFormat)
	{
		return null;
	}
	
	//DTO to Entity
	public static List<AssTipoPastoMensa> mapToEntity(CreaMensaDTO creaMensaDTO, String dateFormat) throws ParseException
	{
		logger.info("Accesso a mapToEntity classe AssTipoPastoMensaDTO");
		ModelMapper mapper = new ModelMapper();
		//SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateFormat);
		
		List<AssTipoPastoMensa> listaAssTipoPastoMensa = new ArrayList<>();
		for(AssTipoPastoMensaDTO assTipoPasto : creaMensaDTO.getAssTipoPastoMensaDTO())
		{
			AssTipoPastoMensa assTipoPastoMensa = new AssTipoPastoMensa();
			
			assTipoPastoMensa.setOrarioDal(ControlloData.controlloTempo(assTipoPasto.getOrarioDal()));
			assTipoPastoMensa.setOrarioAl(ControlloData.controlloTempo(assTipoPasto.getOrarioAl()));
			assTipoPastoMensa.setOraFinePrenotazione(ControlloData.controlloTempo(assTipoPasto.getOraFinePrenotazione()));
			assTipoPastoMensa.setTipoPasto(mapper.map(assTipoPasto.getTipoPastoDTO(), TipoPasto.class));
			
			listaAssTipoPastoMensa.add(assTipoPastoMensa);
		}
		
		return listaAssTipoPastoMensa;
	}
}
