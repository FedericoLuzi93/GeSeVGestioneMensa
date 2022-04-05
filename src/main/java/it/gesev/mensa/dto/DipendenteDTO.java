package it.gesev.mensa.dto;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DipendenteDTO implements Serializable
{
	
	private static final long serialVersionUID = 977691810718555831L;
	
	private Integer codiceDipendente;
	private String nome;
	private String cognome;
	private String codiceFiscale;
	private String tipoPersonale;
	private String grado;
	private String email;
}
