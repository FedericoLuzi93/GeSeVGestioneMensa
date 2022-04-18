package it.gesev.mensa.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PietanzaDTO 
{
	private Integer idPietanza;
	private String descrizionePietanza;
	private Integer tipoPietanza;
	private Integer tipoPasto;
}
