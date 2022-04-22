package it.gesev.mensa.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DC4RichiestaDTO 
{
	private String sistemaPersonale;
	private String anno;
	private String mese;
	private String giorno;
	private int tipoPasto;
	private int idEnte;
	private int idOperatore;
}
