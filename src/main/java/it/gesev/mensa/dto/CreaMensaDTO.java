package it.gesev.mensa.dto;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

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
	//private byte[] autorizzazioneSanitaria;
	private String numeroAutorizzazioneSanitaria;
	private String dataAutorizzazioneSanitaria;
	private String autSanitariaRilasciataDa;
	private String oraFinePrenotazione;
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
	
	//Liste Enti, AssMensaTipoLocale e CodiceTipoFormaVettovagliamento
	private Integer codiceTipoFormaVettovagliamento;
	private List<EnteDTO> listaEntiDTO;
	private List<AssMensaTipoLocaleDTO> assMensaTipoLocaleDTO;
	
	@Override
	public String toString() {
		return "CreaMensaDTO [codiceMensa=" + codiceMensa + ", descrizioneMensa=" + descrizioneMensa + ", orarioDal="
				+ orarioDal + ", orarioAl=" + orarioAl + ", servizioFestivo=" + servizioFestivo
				+ ", numeroAutorizzazioneSanitaria=" + numeroAutorizzazioneSanitaria + ", dataAutorizzazioneSanitaria="
				+ dataAutorizzazioneSanitaria + ", autSanitariaRilasciataDa=" + autSanitariaRilasciataDa
				+ ", oraFinePrenotazione=" + oraFinePrenotazione + ", dataInizioServizio=" + dataInizioServizio
				+ ", dataFineServizio=" + dataFineServizio + ", via=" + via + ", numeroCivico=" + numeroCivico
				+ ", cap=" + cap + ", citta=" + citta + ", provincia=" + provincia + ", telefono=" + telefono + ", fax="
				+ fax + ", email=" + email + ", codiceTipoFormaVettovagliamento=" + codiceTipoFormaVettovagliamento
				+ ", listaEntiDTO=" + listaEntiDTO + ", assMensaTipoLocaleDTO=" + assMensaTipoLocaleDTO + "]";
	}
	

}
