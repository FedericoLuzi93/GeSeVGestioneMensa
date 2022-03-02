package it.gesev.mensa.dto;

import java.io.Serializable;
import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class FirmaDTO implements Serializable
{
	private static final long serialVersionUID = -7862928031716979173L;
	private String idReport;
	private List<OrdineFirmaDTO> listaForma;

}
