package it.gesev.mensa.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DC4TabellaAllegatoCDTO 
{
	private String giorno;
	private Integer numpranziUSC;
	private Integer numCeneUSC;
	private Integer numColazioniGraduati;
	private Integer numPranziGraduati;
	private Integer numCeneGraduati;
}
