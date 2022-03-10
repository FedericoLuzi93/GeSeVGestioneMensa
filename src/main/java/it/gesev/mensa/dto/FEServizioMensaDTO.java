package it.gesev.mensa.dto;

import java.util.List;

import lombok.Data;

@Data
public class FEServizioMensaDTO 
{
	private String dataInizioServizio;
	private String dataFineServizio;
	private List<TipoPastoDTO> listaTipoPastoDTO;
	//private AssTipoPastoMensaDTO assTipoPastoMensaDTO;
}
