package it.gesev.mensa.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AssMensaTipoLocaleDTO 
{
	private Integer assMensaTipoLocaleId;
	private String dataInizio;
	private String dataFine;
	private Integer superficie;
	private Integer numeroLocali;
	private String note;
	private MensaDTO mensaDTO;
	private TipoLocaleDTO tipoLocaleDTO;
	
}
