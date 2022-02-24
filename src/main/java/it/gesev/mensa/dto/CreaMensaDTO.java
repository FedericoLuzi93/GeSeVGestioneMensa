package it.gesev.mensa.dto;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreaMensaDTO 
{
	//Base
	private Integer codiceMensa;
	private String descrizioneMensa;
	private String orarioDal;
	private String orarioAl;
	private String servizioFestivo;
	private byte[] autorizzazioneSanitaria;
	private String numeroAutorizzazioneSanitaria;
	private String dataAutorizzazioneSanitaria;
	private String autSanitariaRilasciataDa;
	private String oraFinePrenotazione;
	
	//Contatti
	private String via;
	private Integer numeroCivico;
	private String cap;
	private String citta;
	private String provincia;
	private String telefono;
	private String fax;
	private String email;
	
	//Liste Enti e Locali
	private List<TipoLocaleDTO> listaTipoLocaliDTO;
	private List<EnteDTO> listaEntiDTO;
	

}