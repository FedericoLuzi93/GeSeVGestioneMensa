package it.gesev.mensa.dto;

import it.gesev.mensa.entity.Mensa;
import lombok.Data;

@Data
public class ServizioEventoDTO 
{
	private Integer idServizioEvento;
	private String dataServizioEvento;
	private String descrizioneServizioEvento;
	//private Mensa mensa;
}
