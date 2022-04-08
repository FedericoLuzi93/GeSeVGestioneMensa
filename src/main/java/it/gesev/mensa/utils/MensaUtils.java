package it.gesev.mensa.utils;

import org.apache.commons.lang3.StringUtils;

import it.gesev.mensa.exc.GesevException;

public class MensaUtils 
{
	public static String convertiMese(String mese)
	{
		String meseConvertito = "";

		if(StringUtils.isBlank(mese))
			throw new GesevException("Errore nella conversione del mese, il campo è vuoto o nullo");

		switch(mese)
		{
		case "01" :
			meseConvertito = "Gennaio";
			break;
		case "02" :
			meseConvertito = "Febbraio";
			break;
		case "03" :
			meseConvertito = "Marzo";
			break;
		case "04" :
			meseConvertito = "Aprile";
			break;
		case "05" :
			meseConvertito = "Maggio";
			break;
		case "06" :
			meseConvertito = "Giugno";
			break;
		case "07" :
			meseConvertito = "Luglio";
			break;
		case "08" :
			meseConvertito = "Agosto";
			break;
		case "09" :
			meseConvertito = "Settembre";
			break;
		case "10" :
			meseConvertito = "Ottobre";
			break;
		case "11" :
			meseConvertito = "Novembre";
			break;
		case "12" :
			meseConvertito = "Dicembre";
			break;
		}
		
		return meseConvertito;
	}

}
