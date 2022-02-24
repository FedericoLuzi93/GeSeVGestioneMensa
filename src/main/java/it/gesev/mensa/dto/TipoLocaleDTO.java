package it.gesev.mensa.dto;

import java.util.List;

import it.gesev.mensa.entity.AssMensaTipoLocale;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TipoLocaleDTO 
{
	private Integer codiceTipoLocale;
	private String descrizioneTipoLocale;
	private List<AssMensaTipoLocale> listaAssDipendenteRuolo;
}
