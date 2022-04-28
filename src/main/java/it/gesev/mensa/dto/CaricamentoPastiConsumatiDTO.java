package it.gesev.mensa.dto;

import com.opencsv.bean.CsvBindByPosition;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CaricamentoPastiConsumatiDTO 
{
	private Integer idPastiConsumati;
	
	@CsvBindByPosition(position = 1)
	private String dataPasto;
	
	@CsvBindByPosition(position = 3)
	private String cognome;
	
	@CsvBindByPosition(position = 2)
	private String nome;
	
	@CsvBindByPosition(position = 4)
	private String codiceFiscale;
	
	@CsvBindByPosition(position = 5)
	private String cmd;
	
	@CsvBindByPosition(position = 6)
	private String tipoPersonale;
	
	@CsvBindByPosition(position = 9)
	private String oraIngresso;
	
	@CsvBindByPosition(position = 7)
	private String tipoPagamento;
	
	@CsvBindByPosition(position = 8)
	private int tipoPasto;
	
	@CsvBindByPosition(position = 0)
	private int mensa;
	
	@CsvBindByPosition(position = 11)
	private String identificativoSistema;
	
	@CsvBindByPosition(position = 10)
	private String tipoRazione;
	

}
