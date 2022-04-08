package it.gesev.mensa.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DC4TabellaDTO 
{
	private String giorno;
	private Integer colazioneEffettiva;
	private Integer pranzoEffettiva;
	private Integer cenaEffettiva;
	private Integer colazioneOrdinati;
	private Integer pranzoOrdinati;
	private Integer cenaOridnati;
	private Integer colazioneConsumati;
	private Integer pranzoConsumati;
	private Integer cenaConsumati;
	private String firma;
	private String descrizioneEnte;

}
