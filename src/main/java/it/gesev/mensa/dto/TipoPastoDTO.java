package it.gesev.mensa.dto;

import lombok.Data;

@Data
public class TipoPastoDTO 
{
	private Integer codiceTipoPasto;
	private String descrizione;
	
	private String orarioDal;
	private String orarioAl;
	private String oraFinePrenotazione;
}
