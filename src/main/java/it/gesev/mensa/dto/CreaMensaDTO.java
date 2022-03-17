package it.gesev.mensa.dto;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreaMensaDTO 
{
	//Base
	private int idEnte;
	
	private Integer codiceMensa;
	private String descrizioneMensa;
//	private String servizioFestivo;
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
	
	//codiceVett, TipoLocale, TipoPasto, ServizioEvento, TipoMensa
	private int codiceTipoFormaVettovagliamento;
	private List<TipoLocaleDTO> listaTipoLocaleDTO;
	private List<TipoPastoDTO> listaTipoPastoDTO;	
	private List<ServizioEventoDTO> listaServizioEventoDTO;
	private List<TipoDietaDTO> listaTipoDietaDTO;
	
	@Override
	public String toString() {
		return "CreaMensaDTO [idEnte=" + idEnte + ", codiceMensa=" + codiceMensa + ", descrizioneMensa="
				+ descrizioneMensa + ", servizioFestivoSabato=" + servizioFestivoSabato + ", servizioFestivoDomenica="
				+ servizioFestivoDomenica + ", dataAutorizzazioneSanitaria=" + dataAutorizzazioneSanitaria
				+ ", numeroAutorizzazioneSanitaria=" + numeroAutorizzazioneSanitaria + ", autSanitariaRilasciataDa="
				+ autSanitariaRilasciataDa + ", dataInizioServizio=" + dataInizioServizio + ", dataFineServizio="
				+ dataFineServizio + ", via=" + via + ", numeroCivico=" + numeroCivico + ", cap=" + cap + ", citta="
				+ citta + ", provincia=" + provincia + ", telefono=" + telefono + ", fax=" + fax + ", email=" + email
				+ ", codiceTipoFormaVettovagliamento=" + codiceTipoFormaVettovagliamento + ", listaTipoLocaleDTO="
				+ listaTipoLocaleDTO + ", listaTipoPastoDTO=" + listaTipoPastoDTO + ", listaServizioEventoDTO="
				+ listaServizioEventoDTO + ", listaTipoDietaDTO=" + listaTipoDietaDTO + "]";
	}
	


	
	


}
