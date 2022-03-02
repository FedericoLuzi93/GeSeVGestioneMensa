package it.gesev.mensa.dto;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrdineFirmaDTO implements Serializable
{
	private static final long serialVersionUID = 7657746253241611275L;
	private Integer idRuolo;
	private Integer ordineFirma;
}
