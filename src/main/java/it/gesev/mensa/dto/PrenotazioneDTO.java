package it.gesev.mensa.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PrenotazioneDTO 
{
	private String sistemaPersonale;
	//private String denominazioneEnte;
	private String denominazioneMensa;
	private String dataPrenotazione; 
	private String codiceFiscale;
	private String nomeCognome;
	private String tipoPersonale;
	private String grado;
	private String tipoPasto;
	private String flagCestino;
	private String tipoDieta;
	private String tipoRazione;
}
