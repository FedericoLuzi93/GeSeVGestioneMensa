package it.gesev.mensa.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PastiPrenotatiDTO
{
	
	private String identificativoSistema;
	private String descrizioneMensa;
	private String dataPrenotazione;
	private String codiceFiscale;
	private String nome;
	private String cognome;
	private String tipoPersonale;
	private String grado;
	private String tipoGrado;
	private String strutturaOrganizzativa;
	private String denominazioneUnitaFunzionale;
	private String commensaleEsterno;
	private String tipoPagamento;
	private String tipoPasto;
	private String flagCestino;
	private String tipoDieta;
	private String tipoRazione;
	private String specchioFlag;
	private String colObbligatoriaFlag;

}
