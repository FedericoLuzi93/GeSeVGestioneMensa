package it.gesev.mensa.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MenuLeggeroDTO 
{
	private Integer idMensa;
	private String dataMenu;
	private Integer tipoDieta;
	private Integer tipoPasto;
}
