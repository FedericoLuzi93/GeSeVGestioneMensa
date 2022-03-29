package it.gesev.mensa.dto;

import java.util.List;

import lombok.Data;

@Data
public class FEMensaCompletaDTO 
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
	
	private List<FELocaliDTO> listaTipoLocaleDTO;
	//private List<TipoLocaleDTO> listaTipoLocaleDTO;
	private List<TipoPastoDTO> listaTipoPastoDTO;	
	private List<ServizioEventoDTO> listaServizioEventoDTO;
	private List<TipoDietaDTO> listaTipoDietaDTO;

}
