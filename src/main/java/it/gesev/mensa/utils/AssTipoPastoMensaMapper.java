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

	//DTO to Entity con ListaPassata
//	public static List<AssTipoPastoMensa> mapToEntityConLista(CreaMensaDTO creaMensaDTO, String dateFormat, List<TipoPasto> listaTipoPasto) throws ParseException
//	{
//		logger.info("Accesso a mapToEntity classe AssTipoPastoMensaDTO");
//		ModelMapper mapper = new ModelMapper();
//
//		List<AssTipoPastoMensa> listaAssTipoPastoMensa = new ArrayList<>();
//		List<TipoPasto> listaTipoPasti = listaTipoPasto;
//		for(AssTipoPastoMensaDTO assTipoPasto : creaMensaDTO.getAssTipoPastoMensaDTO())
//		{
//			AssTipoPastoMensa assTipoPastoMensa = new AssTipoPastoMensa();
//
//			if(assTipoPasto.getOrarioDal() != null)
//				assTipoPastoMensa.setOrarioDal(ControlloData.controlloTempo(assTipoPasto.getOrarioDal()));
//
//			if(assTipoPasto.getOrarioAl() != null)
//				assTipoPastoMensa.setOrarioAl(ControlloData.controlloTempo(assTipoPasto.getOrarioAl()));
//
//			if(assTipoPasto.getOraFinePrenotazione() != null)
//				assTipoPastoMensa.setOraFinePrenotazione(ControlloData.controlloTempo(assTipoPasto.getOraFinePrenotazione()));
//
//			for(TipoPasto tp : listaTipoPasti)
//			{
//				if(assTipoPasto.getTipoPastoDTO().getCodiceTipoPasto() == tp.getCodiceTipoPasto())
//					assTipoPastoMensa.setTipoPasto(tp);
//			}
//
//			listaAssTipoPastoMensa.add(assTipoPastoMensa);
//		}
//
//		return listaAssTipoPastoMensa;
//	}

	//DTO to Entity
	public static List<AssTipoPastoMensa> mapToEntity(CreaMensaDTO creaMensaDTO, String dateFormat) throws ParseException
	{
		logger.info("Accesso a mapToEntity classe AssTipoPastoMensaDTO");
		ModelMapper mapper = new ModelMapper();

//		List<AssTipoPastoMensa> listaAssTipoPastoMensa = new ArrayList<>();
//		for(AssTipoPastoMensaDTO assTipoPasto : creaMensaDTO.get())
//		{
//			AssTipoPastoMensa assTipoPastoMensa = new AssTipoPastoMensa();
//
//			if(assTipoPasto.getOrarioDal() != null)
//				assTipoPastoMensa.setOrarioDal(ControlloData.controlloTempo(assTipoPasto.getOrarioDal()));
//
//			if(assTipoPasto.getOrarioAl() != null)
//				assTipoPastoMensa.setOrarioAl(ControlloData.controlloTempo(assTipoPasto.getOrarioAl()));
//
//			if(assTipoPasto.getOraFinePrenotazione() != null)
//				assTipoPastoMensa.setOraFinePrenotazione(ControlloData.controlloTempo(assTipoPasto.getOraFinePrenotazione()));
//
//			//assTipoPastoMensa.setTipoPasto(mapper.map(assTipoPasto.getTipoPastoDTO(), TipoPasto.class));
//
//			listaAssTipoPastoMensa.add(assTipoPastoMensa);
//		}

		return null;
	}


}
