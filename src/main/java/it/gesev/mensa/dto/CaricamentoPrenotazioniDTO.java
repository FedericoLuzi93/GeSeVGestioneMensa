package it.gesev.mensa.dto;

import com.opencsv.bean.CsvBindByPosition;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CaricamentoPrenotazioniDTO 
{
	@CsvBindByPosition(position = 0)
	private String identificativoSistema;
	
	@CsvBindByPosition(position = 1)
	private String mensa;
	
	@CsvBindByPosition(position = 2)
	private String dataPrenotazione;
	
	@CsvBindByPosition(position = 3)
	private String codiceFiscale;
	
	@CsvBindByPosition(position = 4)
	private String nome;
	
	@CsvBindByPosition(position = 5)
	private String cognome;
	
	@CsvBindByPosition(position = 6)
	private String tipoPersonale;
	
	@CsvBindByPosition(position = 7)
	private String grado;
	
	@CsvBindByPosition(position = 8)
	private String tipoGrado;
	
	@CsvBindByPosition(position = 9)
	private String strutturaOrganizzativa;
	
	@CsvBindByPosition(position = 10)
	private String denominazioneUnitaFunzionale;
	
	@CsvBindByPosition(position = 11)
	private String commensaleEsterno;
	
	@CsvBindByPosition(position = 12)
	private String tipoPagamento;
	
	@CsvBindByPosition(position = 13)
	private String tipoPasto;
	
	@CsvBindByPosition(position = 14)
	private String flagCestino;
	
	@CsvBindByPosition(position = 15)
	private String tipoDieta;
	
	@CsvBindByPosition(position = 16)
	private String tipoRazione;
	
	@CsvBindByPosition(position = 17)
	private String specchioFlag;
	
	@CsvBindByPosition(position = 18)
	private String colObbligatoriaFlag;
	
	
}
