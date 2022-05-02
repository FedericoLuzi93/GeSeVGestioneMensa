package it.gesev.mensa.dto;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FornitoreDTO implements Serializable
{
	private static final long serialVersionUID = -3670370691336670362L;
	private Long codice;
	private String Descrizione;
	private String recapito;
	private String piCf;
	
}
