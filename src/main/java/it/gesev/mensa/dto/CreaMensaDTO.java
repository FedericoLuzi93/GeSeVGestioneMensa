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
	private String servizioFestivo;
	private String servizioFestivoSabato;
	private String servizioFestivoDomenica;
	private String dataAutorizzazioneSanitaria;
	private String numeroAutorizzazioneSanitaria;
	private String autSanitariaRilasciataDa;
	private String dataInizioServizio;
	private String dataFineServizio;
	
	//Contatti
	private String via;
	private Integer numeroCivico;
	private String cap;
	private String citta;
	private String provincia;
	private String telefono;
	private String fax;
	private String email;
	
	//Liste Enti, AssMensaTipoLocale, AssTipoPastoMensa e descrizioneTipoFormaVettovagliamento
	private String descrizioneTipoFormaVettovagliamento;
	private List<EnteDTO> listaEntiDTO;
	private List<AssMensaTipoLocaleDTO> assMensaTipoLocaleDTO;	
	private List<AssTipoPastoMensaDTO> assTipoPastoMensaDTO;
	
	@Override
	public String toString() {
		return "CreaMensaDTO [codiceMensa=" + codiceMensa + ", descrizioneMensa=" + descrizioneMensa
				+ ", servizioFestivo=" + servizioFestivo + ", numeroAutorizzazioneSanitaria="
				+ numeroAutorizzazioneSanitaria + ", dataAutorizzazioneSanitaria=" + dataAutorizzazioneSanitaria
				+ ", autSanitariaRilasciataDa=" + autSanitariaRilasciataDa + ", dataInizioServizio="
				+ dataInizioServizio + ", dataFineServizio=" + dataFineServizio + ", via=" + via + ", numeroCivico="
				+ numeroCivico + ", cap=" + cap + ", citta=" + citta + ", provincia=" + provincia + ", telefono="
				+ telefono + ", fax=" + fax + ", email=" + email + ", descrizioneTipoFormaVettovagliamento="
				+ descrizioneTipoFormaVettovagliamento + ", listaEntiDTO=" + listaEntiDTO + ", assMensaTipoLocaleDTO="
				+ assMensaTipoLocaleDTO + ", assTipoPastoMensaDTO=" + assTipoPastoMensaDTO + "]";
	}
}
