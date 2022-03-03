package it.gesev.mensa.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MensaDTO 
{
	//Mensa
	private Integer codiceMensa;
	private String descrizioneMensa;
	private String orarioDal;
	private String orarioAl;
	private String servizioFestivo;
	//private byte[] autorizzazioneSanitaria;
	private String numeroAutorizzazioneSanitaria;
	private String dataAutorizzazioneSanitaria;
	private String autSanitariaRilasciataDa;
	private String oraFinePrenotazione;
	private String dataInizioServizio;
	private String dataFineServizio;
	private String descrizioneTipoFormaVettovagliamento;
	
	//Contatti
	private String via;
	private Integer numeroCivico;
	private String cap;
	private String citta;
	private String provincia;
	private String telefono;
	private String fax;
	private String email;
}
