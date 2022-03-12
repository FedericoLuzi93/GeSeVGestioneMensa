package it.gesev.mensa.utils;

import java.time.LocalTime;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.el.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ControlloData 
{
	private static final Logger logger = LoggerFactory.getLogger(ControlloData.class);
	
	public static LocalTime controlloTempo(String tempo) throws ParseException
	{ 
		LocalTime lt = null;
		Pattern pattern = Pattern.compile(tempo);
		Matcher matcher = pattern.matcher("^[0-9]{2}:[0-9]{2}$");
		if(!matcher.matches())
		{
			logger.info("Orario " + tempo + " non e' nel formato HH:mm");
			throw new ParseException("Orario " + tempo + " non e' nel formato HH:mm");
		}
		String[] divisione = tempo.split(":");
		String ore = divisione[0];
		String minuti = divisione[1];
			
		int oreNumero = Integer.parseInt(ore);
		int minutiNumero = Integer.parseInt(minuti);
			
		//controllo numerico
		lt = LocalTime.of(oreNumero, minutiNumero);

		
		return lt;
	}
	


}
