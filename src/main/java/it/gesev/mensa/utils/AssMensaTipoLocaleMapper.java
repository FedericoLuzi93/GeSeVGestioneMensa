package it.gesev.mensa.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import it.gesev.mensa.dto.AssMensaTipoLocaleDTO;
import it.gesev.mensa.dto.CreaMensaDTO;
import it.gesev.mensa.entity.AssMensaTipoLocale;
import it.gesev.mensa.entity.TipoLocale;

public class AssMensaTipoLocaleMapper 
{
	private static final Logger logger = LoggerFactory.getLogger(AssMensaTipoLocaleMapper.class);

	//Entity to DTO
	public static AssMensaTipoLocaleDTO mapToDTO(AssMensaTipoLocale assMensaTipoLocale, String dateFormat)
	{
		return null;
	}
	
	//DTO to Entity
	public static List<AssMensaTipoLocale> mapToEntity(CreaMensaDTO creaMensaDTO, String dateFormat) throws ParseException
	{
		logger.info("Accesso a mapToEntity, classe MensaMapper");
		ModelMapper mapper = new ModelMapper();
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateFormat);
		
		List<AssMensaTipoLocale> listaAssMensaTipoLocale = new ArrayList<>();
		for(AssMensaTipoLocaleDTO amtld : creaMensaDTO.getAssMensaTipoLocaleDTO())
		{
			String dataInizio = amtld.getDataInizio();
			String dataFine = creaMensaDTO.getDataFineServizio();
		
			Date dataInizioConvertita = simpleDateFormat.parse(dataInizio);
			Date dataFineConvertita = simpleDateFormat.parse(dataFine);
			
			AssMensaTipoLocale assMensaTipoLocale = new AssMensaTipoLocale();
			assMensaTipoLocale.setTipoLocale(mapper.map(amtld.getTipoLocaleDTO(), TipoLocale.class));
			
			assMensaTipoLocale.setDataInizio(dataInizioConvertita);
			assMensaTipoLocale.setDataFine(dataFineConvertita);
			assMensaTipoLocale.setSuperficie(amtld.getSuperficie());
			assMensaTipoLocale.setNumeroLocali(amtld.getNumeroLocali());
			assMensaTipoLocale.setNote(amtld.getNote());
			
			listaAssMensaTipoLocale.add(assMensaTipoLocale);
		}
		
		
		
		return listaAssMensaTipoLocale;
	}
}