package it.gesev.mensa.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.apache.commons.lang3.StringUtils;
import it.gesev.mensa.dto.CreaMensaDTO;
import it.gesev.mensa.dto.MensaDTO;
import it.gesev.mensa.entity.Mensa;
import it.gesev.mensa.exc.GesevException;

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
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateFormat);
		Mensa mensa = new Mensa();
		//Mensa mensa= mapper.map(creaMensaDTO, Mensa.class);
		//CampiBase
		mensa.setDescrizioneMensa(creaMensaDTO.getDescrizioneMensa());

		//		if(!StringUtils.isBlank(creaMensaDTO.getServizioFestivo()))
		//			mensa.setServizioFestivo(creaMensaDTO.getServizioFestivo());

		if(!StringUtils.isBlank(creaMensaDTO.getServizioFestivoSabato()))
			mensa.setServizioFestivoSabato(creaMensaDTO.getServizioFestivoSabato());

		if(!StringUtils.isBlank(creaMensaDTO.getServizioFestivoDomenica()))
			mensa.setServizioFestivoDomenica(creaMensaDTO.getServizioFestivoDomenica());

		mensa.setDataInizioServizio(simpleDateFormat.parse(creaMensaDTO.getDataInizioServizio()));
		mensa.setDataFineServizio(simpleDateFormat.parse(creaMensaDTO.getDataFineServizio()));

		if(mensa.getDataInizioServizio().after(mensa.getDataFineServizio()))
			throw new GesevException("Errore nelle date della mansa, data inizio servizio o data fine servizio non valida");
		
		if(mensa.getDataInizioServizio().equals(mensa.getDataFineServizio()))
			throw new GesevException("Errore nelle date della mansa, la data inizio servizio e la data fine servizio non possono essere uguali");

		//Contatti

		if(!StringUtils.isBlank(creaMensaDTO.getCap()))
		{
			Pattern pattern = Pattern.compile("^\\d{5}$");
			Matcher matcher = pattern.matcher(creaMensaDTO.getCap());
			if(matcher.matches())
				mensa.setCap(creaMensaDTO.getCap());
			else
				throw new GesevException("Errore nel controllo del CAP, inserire un CAP valido");
		}



		if(!StringUtils.isBlank(creaMensaDTO.getCitta()))
			mensa.setCitta(creaMensaDTO.getCitta());

		if(creaMensaDTO.getNumeroCivico() != null && creaMensaDTO.getNumeroCivico() != 0)
			mensa.setNumeroCivico(creaMensaDTO.getNumeroCivico());

		if(!StringUtils.isBlank(creaMensaDTO.getVia()))
			mensa.setVia(creaMensaDTO.getVia());

		if(!StringUtils.isBlank(creaMensaDTO.getProvincia()))
			mensa.setProvincia(creaMensaDTO.getProvincia());

		
		if(!StringUtils.isBlank(creaMensaDTO.getTelefono()))
		{
			Pattern pattern = Pattern.compile("^\\d{5,}$");
			Matcher matcher = pattern.matcher(creaMensaDTO.getTelefono());
			if(matcher.matches())
				mensa.setTelefono(creaMensaDTO.getTelefono());
			else
				throw new GesevException("Errore nel controllo del Telefono, inserire un Telefono valido");
		}
			
		if(!StringUtils.isBlank(creaMensaDTO.getFax()))
		{
			Pattern pattern = Pattern.compile("^\\d{5,}$");
			Matcher matcher = pattern.matcher(creaMensaDTO.getFax());
			if(matcher.matches())
				mensa.setFax(creaMensaDTO.getFax());
			else
				throw new GesevException("Errore nel controllo del Fax, inserire un Fax valido");
		}

		if(!StringUtils.isBlank(creaMensaDTO.getEmail()))
			mensa.setEmail(creaMensaDTO.getEmail());

		//Autorizzazione Sanitaria
		if(!StringUtils.isBlank(creaMensaDTO.getNumeroAutorizzazioneSanitaria()))
			mensa.setNumeroAutorizzazioneSanitaria(creaMensaDTO.getNumeroAutorizzazioneSanitaria());

		if(!StringUtils.isBlank(creaMensaDTO.getDataAutorizzazioneSanitaria()))
		{
			String dataString = creaMensaDTO.getDataAutorizzazioneSanitaria();
			Date date = simpleDateFormat.parse(dataString);
			mensa.setDataAutorizzazioneSanitaria(date);
		}

		if(!StringUtils.isBlank(creaMensaDTO.getAutSanitariaRilasciataDa()))
			mensa.setAutSanitariaRilasciataDa(creaMensaDTO.getAutSanitariaRilasciataDa());



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
