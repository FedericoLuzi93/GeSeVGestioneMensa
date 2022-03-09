package it.gesev.mensa.dto;

import it.gesev.mensa.entity.AssTipoPastoMensa;
import it.gesev.mensa.entity.TipoPasto;
import lombok.Data;

@Data
public class FEServizioMensaDTO 
{
	private String dataInizioServizio;
	private String dataFineServizio;
	private TipoPasto tipoPasto;
	private AssTipoPastoMensa assTipoPastoMensa;
}
