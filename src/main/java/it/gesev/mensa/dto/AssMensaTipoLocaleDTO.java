package it.gesev.mensa.dto;

import java.util.Date;

import it.gesev.mensa.entity.Mensa;
import it.gesev.mensa.entity.TipoLocale;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AssMensaTipoLocaleDTO 
{
	private Integer assMensaTipoLocaleId;
	private Date dataInizio;
	private Date dataFine;
	private Integer superficie;
	private Integer numeroLocali;
	private String note;
	private Mensa mensa;
	private TipoLocale tipoLocale;
	
}
