package it.gesev.mensa.dto;

import java.util.List;

import it.gesev.mensa.jasper.DC1NomJasper;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FEDC1Nominativo 
{
	private String sistemaGestione;
	private Integer ufficiali;
	private Integer sottoUfficiali;
	private Integer civili;
	private Integer graduati;
	private Integer cestini;
	private Integer totale;
	private List<DC1NomJasper> listaDC1Nominativi;

}
