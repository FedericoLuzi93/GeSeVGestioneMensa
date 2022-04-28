package it.gesev.mensa.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StrutturaOrganizzativaDTO 
{
	private String codiceStrutturaOrganizzativa;
	private String descrizioneStrutturaOrganizzativaB;
	private String descrizioneStrutturaOrganizzativaR;
	private String numero_ordine;
	//private List<Prenotazione> listaPrenotazioni;

}
