package it.gesev.mensa.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GradoDTO 
{
	

	private String shsgraCodUidPk;
	private String shsorCodUidPk;
	private String shcmcCodUidPk;
	private String descbGrado;
	private String descrGrado;
	private String numOrdine;
	private String dataInizio;
	private String dataFine;
	private TipoGradoDTO tipoGradoDTO;
	//private List<Prenotazione> listaPrenotazioni;

}
