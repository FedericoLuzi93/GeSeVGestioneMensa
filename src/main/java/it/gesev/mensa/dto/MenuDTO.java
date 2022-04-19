package it.gesev.mensa.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MenuDTO 
{
	private Integer idMenu;
	private Integer idMensa;
	private String dataMenu;
	private Integer tipoDieta;
	List<PietanzaDTO> listaPietanze;
	List<String> dateSettimana;
	
}
