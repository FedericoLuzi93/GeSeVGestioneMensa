package it.gesev.mensa.dto;

import java.time.LocalTime;
import java.util.Date;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MensaDTO 
{
	private Integer codiceMensa;
	private String descrizioneMensa;
	private LocalTime orarioDal;
	private LocalTime orarioAl;
	private String servizioFestivo;
	private byte[] autorizzazioneSanitaria;
	private String numeroAutorizzazioneSanitaria;
	private String dataAutorizzazioneSanitaria;
	private String autSanitariaRilasciataDa;
	private LocalTime oraFinePrenotazione;
	private String via;
	private Integer numeroCivico;
	private String cap;
	private String citta;













}
