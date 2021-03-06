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
	private String numeroAutorizzazioneSanitaria;
	private String dataAutorizzazioneSanitaria;
	private String autSanitariaRilasciataDa;
	private String dataInizioServizio;
	private String dataFineServizio;
	private String descrizioneTipoFormaVettovagliamento;
	private Integer codiceTipoFormaVettovagliamento;
//	private String tipoDieta;
	
	//Contatti
	private String via;
	private Integer numeroCivico;
	private String cap;
	private String citta;
	private String provincia;
	private String telefono;
	private String fax;
	private String email;
	
	//Servizii Festivi
//	private String servizioFestivo;
	private String servizioFestivoSabato;
	private String servizioFestivoDomenica;
	
	//Controlli Aggiuntivi
	private boolean presenzaFile;
	private String descrizioneEnte;
	private int idEnte;
	
	//Fornitore
	private int idFornitore;
	private String descrizioneFornitore;
	private String recapitoFornitore;
	private String partitaIvaCFFornitore;
}
