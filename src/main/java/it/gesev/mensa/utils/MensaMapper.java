package it.gesev.mensa.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import antlr.StringUtils;
import it.gesev.mensa.dto.CreaMensaDTO;
import it.gesev.mensa.dto.MensaDTO;
import it.gesev.mensa.entity.Mensa;

public class MensaMapper 
{
	private static final Logger logger = LoggerFactory.getLogger(MensaMapper.class);

	//Entity to DTO
	public static MensaDTO mapToDTO(Mensa mensa, String dateFormat)
	{
		logger.info("Accesso a mapToDTO, classe MensaMapper");
		ModelMapper mapper = new ModelMapper();
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateFormat);
		MensaDTO mensaDTO= mapper.map(mensa, MensaDTO.class);
		
		//TipoVettovagliamento	
		mensaDTO.setDescrizioneTipoFormaVettovagliamento(mensa.getTipoFormaVettovagliamento().getDescrizione());
		
		//Date to String
		if(mensa.getDataAutorizzazioneSanitaria() != null)
			mensaDTO.setDataAutorizzazioneSanitaria(simpleDateFormat.format(mensa.getDataAutorizzazioneSanitaria()));
		if(mensa.getDataFineServizio() != null)
			mensaDTO.setDataFineServizio(simpleDateFormat.format(mensa.getDataFineServizio()));
		if(mensa.getDataInizioServizio() != null)
			mensaDTO.setDataInizioServizio(simpleDateFormat.format(mensa.getDataInizioServizio()));
		
		//Controllo presenza file
		if(mensa.getAutorizzazioneSanitaria() != null)
			mensaDTO.setPresenzaFile(true);
		else
			mensaDTO.setPresenzaFile(false);
		
		
		//LocalTime a String
//		mensaDTO.setOrarioAl(mensa.getOrarioAl().toString());
//		mensaDTO.setOrarioDal(mensa.getOrarioDal().toString());
//		mensaDTO.setOraFinePrenotazione(mensa.getOraFinePrenotazione().toString());
		
		return mensaDTO;
	}
	
	//DTO to Entity
	public static Mensa mapToEntity(CreaMensaDTO creaMensaDTO, String dateFormat) throws ParseException
	{
		logger.info("Accesso a mapToEntity, classe MensaMapper");
		ModelMapper mapper = new ModelMapper();
		
		
		String dataString = creaMensaDTO.getDataAutorizzazioneSanitaria();
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateFormat);
		Date date = simpleDateFormat.parse(dataString);
		Mensa mensa= mapper.map(creaMensaDTO, Mensa.class);
		mensa.setDataAutorizzazioneSanitaria(date);
			
		//String to LocalTime
//		mensa.setOrarioAl(ControlloData.controlloTempo(creaMensaDTO.getOrarioAl()));
//		mensa.setOrarioDal(ControlloData.controlloTempo(creaMensaDTO.getOrarioDal()));
//		mensa.setOraFinePrenotazione(ControlloData.controlloTempo(creaMensaDTO.getOraFinePrenotazione()));
		
		return mensa;
	}
	
	//DTO to Entity
	public static Mensa mapToEntityBase(MensaDTO mensaDTO, String dateFormat) throws ParseException
	{
		logger.info("Accesso a mapToEntity, classe MensaMapper");
		ModelMapper mapper = new ModelMapper();
			
		
		String dataString = mensaDTO.getDataAutorizzazioneSanitaria();
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateFormat);
		Date date = simpleDateFormat.parse(dataString);
		Mensa mensa= mapper.map(mensaDTO, Mensa.class);
		mensa.setDataAutorizzazioneSanitaria(date);
			
		//String to LocalTime
//		mensa.setOrarioAl(ControlloData.controlloTempo(mensaDTO.getOrarioAl()));
//		mensa.setOrarioDal(ControlloData.controlloTempo(mensaDTO.getOrarioDal()));
//		mensa.setOraFinePrenotazione(ControlloData.controlloTempo(mensaDTO.getOraFinePrenotazione()));
			
		return mensa;
		
		}
}
